
package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.ReservationDTO;
import ma.youcode.covoiturage.entity.*;
import ma.youcode.covoiturage.mapper.ReservationMapper;
import ma.youcode.covoiturage.repository.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final PassagerRepository passagerRepository;
    private final TrajetRepository trajetRepository;
    private final PointArretRepository pointArretRepository;
    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            PassagerRepository passagerRepository,
            TrajetRepository trajetRepository,
            PointArretRepository pointArretRepository,
            ReservationMapper reservationMapper
    ) {
        this.reservationRepository = reservationRepository;
        this.passagerRepository = passagerRepository;
        this.trajetRepository = trajetRepository;
        this.pointArretRepository = pointArretRepository;
        this.reservationMapper = reservationMapper;
    }

    @Override
    @Transactional
    public ReservationDTO creer(ReservationDTO dto) {

        Passager passager = passagerRepository.findById(dto.passagerId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Passager introuvable"));

        Trajet trajet = trajetRepository.findById(dto.trajetId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Trajet introuvable"));

        PointArret pointMontee = pointArretRepository.findById(dto.pointMonteeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Point de montée introuvable"));

        PointArret pointDescente = pointArretRepository.findById(dto.pointDescenteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Point de descente introuvable"));

        verifierPoints(trajet, pointMontee, pointDescente);
        verifierTrajetDisponible(trajet);

        boolean reservationExiste =
                reservationRepository.existsByPassager_IdAndTrajet_IdAndStatut(
                        passager.getId(),
                        trajet.getId(),
                        StatutReservation.CONFIRMEE
                );

        if (reservationExiste) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce passager possède déjà une réservation confirmée pour ce trajet"
            );
        }

        verifierPlaces(trajet, dto.nombrePlaces());

        BigDecimal prixUnitaire =
                pointDescente.getPrixDepuisDepart()
                        .subtract(pointMontee.getPrixDepuisDepart());

        if (prixUnitaire.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le prix du point de descente doit être supérieur au prix du point de montée"
            );
        }

        BigDecimal montantTotal =
                prixUnitaire.multiply(BigDecimal.valueOf(dto.nombrePlaces()));

        Reservation reservation = new Reservation();

        reservation.setNombrePlaces(dto.nombrePlaces());
        reservation.setMontantTotal(montantTotal);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setPassager(passager);
        reservation.setTrajet(trajet);
        reservation.setPointMontee(pointMontee);
        reservation.setPointDescente(pointDescente);

        Reservation saved = reservationRepository.save(reservation);

        Long placesReservees = reservationRepository.compterPlacesReservees(
                trajet.getId(),
                StatutReservation.CONFIRMEE
        );

        if (placesReservees >= trajet.getPlacesProposees()) {
            trajet.setStatut(StatutTrajet.COMPLET);
            trajetRepository.save(trajet);
        }

        return reservationMapper.toDTO(saved);
    }

    private void verifierPoints(
            Trajet trajet,
            PointArret pointMontee,
            PointArret pointDescente
    ) {

        if (!pointMontee.getTrajet().getId().equals(trajet.getId())
                || !pointDescente.getTrajet().getId().equals(trajet.getId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Les deux points doivent appartenir au même trajet"
            );
        }

        if (pointMontee.getOrdre() >= pointDescente.getOrdre()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le point de montée doit précéder le point de descente"
            );
        }
    }

    private void verifierTrajetDisponible(Trajet trajet) {

        if (trajet.getStatut() != StatutTrajet.PUBLIE) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce trajet n'est pas disponible pour les réservations"
            );
        }
    }

    private void verifierPlaces(Trajet trajet, int nombrePlaces) {

        if (nombrePlaces <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le nombre de places doit être supérieur à zéro"
            );
        }

        Long placesReservees = reservationRepository.compterPlacesReservees(
                trajet.getId(),
                StatutReservation.CONFIRMEE
        );

        int placesRestantes =
                trajet.getPlacesProposees() - placesReservees.intValue();

        if (nombrePlaces > placesRestantes) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Nombre de places insuffisant"
            );
        }
    }

    @Override
    public List<ReservationDTO> trouverTous() {

        return reservationRepository.findAll()
                .stream()
                .map(reservationMapper::toDTO)
                .toList();
    }

    @Override
    public ReservationDTO trouverParId(Long id) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Réservation introuvable"
                ));

        return reservationMapper.toDTO(reservation);
    }

    @Override
    @Transactional
    public ReservationDTO modifier(Long id, ReservationDTO dto) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Réservation introuvable"
                ));

        if (reservation.getStatut() != StatutReservation.CONFIRMEE) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Seule une réservation confirmée peut être modifiée"
            );
        }

        Passager passager = passagerRepository.findById(dto.passagerId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Passager introuvable"));

        Trajet trajet = trajetRepository.findById(dto.trajetId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Trajet introuvable"));

        PointArret pointMontee = pointArretRepository.findById(dto.pointMonteeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Point de montée introuvable"));

        PointArret pointDescente = pointArretRepository.findById(dto.pointDescenteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Point de descente introuvable"));

        verifierPoints(trajet, pointMontee, pointDescente);

        if (trajet.getStatut() != StatutTrajet.PUBLIE
                && trajet.getStatut() != StatutTrajet.COMPLET) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce trajet n'est pas disponible"
            );
        }

        boolean reservationExiste =
                reservationRepository.existsByPassager_IdAndTrajet_IdAndStatutAndIdNot(
                        passager.getId(),
                        trajet.getId(),
                        StatutReservation.CONFIRMEE,
                        id
                );

        if (reservationExiste) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce passager possède déjà une réservation confirmée pour ce trajet"
            );
        }

        Long placesReservees = reservationRepository.compterPlacesReservees(
                trajet.getId(),
                StatutReservation.CONFIRMEE
        );

        if (reservation.getTrajet().getId().equals(trajet.getId())) {
            placesReservees -= reservation.getNombrePlaces();
        }

        int placesRestantes =
                trajet.getPlacesProposees() - placesReservees.intValue();

        if (dto.nombrePlaces() <= 0 || dto.nombrePlaces() > placesRestantes) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Nombre de places insuffisant"
            );
        }

        BigDecimal prixUnitaire =
                pointDescente.getPrixDepuisDepart()
                        .subtract(pointMontee.getPrixDepuisDepart());

        if (prixUnitaire.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le prix du point de descente doit être supérieur au prix du point de montée"
            );
        }

        BigDecimal montantTotal =
                prixUnitaire.multiply(BigDecimal.valueOf(dto.nombrePlaces()));

        Trajet ancienTrajet = reservation.getTrajet();

        reservation.setNombrePlaces(dto.nombrePlaces());
        reservation.setMontantTotal(montantTotal);
        reservation.setPassager(passager);
        reservation.setTrajet(trajet);
        reservation.setPointMontee(pointMontee);
        reservation.setPointDescente(pointDescente);

        Reservation saved = reservationRepository.save(reservation);

        mettreAJourStatutTrajet(ancienTrajet);
        mettreAJourStatutTrajet(trajet);

        return reservationMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void annuler(Long id) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Réservation introuvable"
                ));

        if (reservation.getStatut() != StatutReservation.CONFIRMEE) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cette réservation n'est pas confirmée"
            );
        }

        Trajet trajet = reservation.getTrajet();

        if (LocalDateTime.now().plusHours(24).isAfter(trajet.getDateDepart())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "L'annulation doit être effectuée au moins 24 heures avant le départ"
            );
        }

        reservation.setStatut(StatutReservation.ANNULEE);
        reservationRepository.save(reservation);

        mettreAJourStatutTrajet(trajet);
    }

    private void mettreAJourStatutTrajet(Trajet trajet) {

        if (trajet.getStatut() == StatutTrajet.ANNULE
                || trajet.getStatut() == StatutTrajet.TERMINE) {
            return;
        }

        Long placesReservees = reservationRepository.compterPlacesReservees(
                trajet.getId(),
                StatutReservation.CONFIRMEE
        );

        if (placesReservees >= trajet.getPlacesProposees()) {
            trajet.setStatut(StatutTrajet.COMPLET);
        } else {
            trajet.setStatut(StatutTrajet.PUBLIE);
        }

        trajetRepository.save(trajet);
    }

    @Override
    @Transactional
    public void supprimer(Long id) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Réservation introuvable"
                ));

        Trajet trajet = reservation.getTrajet();

        reservationRepository.delete(reservation);

        mettreAJourStatutTrajet(trajet);
    }
}
