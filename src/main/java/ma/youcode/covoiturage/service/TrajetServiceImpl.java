
package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.RechercheTrajetDTO;
import ma.youcode.covoiturage.dto.TrajetRechercheResultatDTO;
import ma.youcode.covoiturage.entity.*;
import ma.youcode.covoiturage.repository.TrajetRepository;
import ma.youcode.covoiturage.repository.PointArretRepository;
import ma.youcode.covoiturage.repository.ReservationRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrajetServiceImpl implements TrajetService {

    private final TrajetRepository trajetRepository;
    private final PointArretRepository pointArretRepository;
    private final ReservationRepository reservationRepository;

    public TrajetServiceImpl(
            TrajetRepository trajetRepository,
            PointArretRepository pointArretRepository,
            ReservationRepository reservationRepository) {
        this.trajetRepository = trajetRepository;
        this.pointArretRepository = pointArretRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Trajet creer(Trajet trajet) {
        verifierDateDepart(trajet.getDateDepart());
        trajet.setStatut(null);
        return trajetRepository.save(trajet);
    }

    @Override
    public List<Trajet> lister() {
        return trajetRepository.findAll();
    }

    @Override
    public Trajet chercherParId(Long id) {
        return trajetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Trajet introuvable"));
    }

    @Override
    public Trajet modifier(Long id, Trajet trajet) {
        Trajet trajetExistant = chercherParId(id);

        if (trajetExistant.getStatut() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Impossible de modifier un trajet déjà publié");
        }

        verifierDateDepart(trajet.getDateDepart());

        trajetExistant.setVilleDepart(trajet.getVilleDepart());
        trajetExistant.setVilleArrivee(trajet.getVilleArrivee());
        trajetExistant.setDateDepart(trajet.getDateDepart());
        trajetExistant.setPlacesProposees(trajet.getPlacesProposees());
        trajetExistant.setConducteur(trajet.getConducteur());
        trajetExistant.setVehicule(trajet.getVehicule());

        return trajetRepository.save(trajetExistant);
    }

    @Override
    public Trajet publier(Long id) {
        Trajet trajet = chercherParId(id);

        if (trajet.getStatut() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce trajet a déjà été publié");
        }

        verifierDateDepart(trajet.getDateDepart());

        List<PointArret> points = pointArretRepository.findAll()
                .stream()
                .filter(p -> p.getTrajet().getId().equals(id))
                .sorted((p1, p2) -> Integer.compare(
                        p1.getOrdre(), p2.getOrdre()))
                .toList();

        if (points.size() < 2) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Un trajet doit avoir au moins deux points d'arrêt");
        }

        PointArret premier = points.get(0);
        PointArret dernier = points.get(points.size() - 1);

        if (premier.getOrdre() != 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le premier point doit avoir l'ordre 0");
        }

        if (!premier.getVille().equalsIgnoreCase(trajet.getVilleDepart())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La première ville doit être la ville de départ");
        }

        if (!dernier.getVille().equalsIgnoreCase(trajet.getVilleArrivee())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La dernière ville doit être la ville d'arrivée");
        }

        if (premier.getPrixDepuisDepart().compareTo(
                java.math.BigDecimal.ZERO) != 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le prix du premier point doit être 0");
        }

        for (int i = 1; i < points.size(); i++) {
            PointArret precedent = points.get(i - 1);
            PointArret actuel = points.get(i);

            if (actuel.getOrdre() <= precedent.getOrdre()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "L'ordre des points doit être croissant");
            }

            if (!actuel.getHeurePassage().isAfter(precedent.getHeurePassage())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Les heures de passage doivent être croissantes");
            }

            if (actuel.getPrixDepuisDepart().compareTo(
                    precedent.getPrixDepuisDepart()) <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Les prix doivent être strictement croissants");
            }
        }

        long nombreVilles = points.stream()
                .map(p -> p.getVille().toLowerCase())
                .distinct()
                .count();

        if (nombreVilles != points.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Une ville ne peut apparaître qu'une seule fois");
        }

        trajet.setStatut(StatutTrajet.PUBLIE);

        return trajetRepository.save(trajet);
    }

    @Override
    @Transactional
    public void annuler(Long id) {
        Trajet trajet = chercherParId(id);

        if (trajet.getStatut() != StatutTrajet.PUBLIE
                && trajet.getStatut() != StatutTrajet.COMPLET) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Seuls les trajets publiés ou complets peuvent être annulés");
        }

        List<Reservation> reservations = reservationRepository.findAll()
                .stream()
                .filter(r -> r.getTrajet().getId().equals(id))
                .filter(r -> r.getStatut() == StatutReservation.CONFIRMEE)
                .toList();

        reservations.forEach(r ->
                r.setStatut(StatutReservation.ANNULEE));

        reservationRepository.saveAll(reservations);

        trajet.setStatut(StatutTrajet.ANNULE);
        trajetRepository.save(trajet);
    }

    @Override
    public void supprimer(Long id) {
        Trajet trajet = chercherParId(id);
        trajetRepository.delete(trajet);
    }

    @Override
    public Page<TrajetRechercheResultatDTO> rechercher(
            RechercheTrajetDTO criteres,
            Pageable pageable) {

        LocalDateTime debutJournee =
                criteres.dateDepart().atStartOfDay();

        LocalDateTime lendemain =
                debutJournee.plusDays(1);

        return trajetRepository.rechercher(
                criteres.villeDepart(),
                criteres.villeArrivee(),
                debutJournee,
                lendemain,
                criteres.nombrePlaces(),
                criteres.prixMax(),
                StatutTrajet.PUBLIE,
                StatutReservation.CONFIRMEE,
                pageable
        );
    }

    private void verifierDateDepart(LocalDateTime dateDepart) {
        if (!dateDepart.isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La date de départ doit être dans le futur");
        }
    }
}
