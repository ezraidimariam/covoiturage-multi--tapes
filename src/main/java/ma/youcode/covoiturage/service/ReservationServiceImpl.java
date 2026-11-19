package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.ReservationDTO;
import ma.youcode.covoiturage.entity.*;
import ma.youcode.covoiturage.mapper.ReservationMapper;
import ma.youcode.covoiturage.repository.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

        Reservation reservation = reservationMapper.toEntity(
                dto, passager, trajet, pointMontee, pointDescente
        );

        return reservationMapper.toDTO(reservationRepository.save(reservation));
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
                        HttpStatus.NOT_FOUND, "Réservation introuvable"));

        return reservationMapper.toDTO(reservation);
    }

    @Override
    public ReservationDTO modifier(Long id, ReservationDTO dto) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Réservation introuvable"));

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

        reservation.setNombrePlaces(dto.nombrePlaces());
        reservation.setMontantTotal(dto.montantTotal());
        reservation.setDateReservation(dto.dateReservation());
        reservation.setStatut(dto.statut());
        reservation.setPassager(passager);
        reservation.setTrajet(trajet);
        reservation.setPointMontee(pointMontee);
        reservation.setPointDescente(pointDescente);

        return reservationMapper.toDTO(reservationRepository.save(reservation));
    }

    @Override
    public void supprimer(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Réservation introuvable"));

        reservationRepository.delete(reservation);
    }
}