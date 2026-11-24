package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.entity.Passager;
import ma.youcode.covoiturage.entity.Reservation;
import ma.youcode.covoiturage.entity.StatutReservation;
import ma.youcode.covoiturage.repository.PassagerRepository;
import ma.youcode.covoiturage.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

// Indique à Spring que cette classe contient la logique métier des passagers.
@Service
public class PassagerServiceImpl implements PassagerService {

    // Repository utilisé pour accéder aux données des passagers.
    private final PassagerRepository passagerRepository;
    private final ReservationRepository reservationRepository;

    // Injection du Repository par constructeur.
    public PassagerServiceImpl(PassagerRepository passagerRepository, ReservationRepository reservationRepository) {
        this.passagerRepository = passagerRepository;
        this.reservationRepository = reservationRepository;
    }

    // Crée un nouveau passager et l'enregistre dans la base de données.
    @Override
    public Passager creer(Passager passager) {
        return passagerRepository.save(passager);
    }

    // Récupère la liste de tous les passagers.
    @Override
    public List<Passager> lister() {
        return passagerRepository.findAll();
    }

    // Recherche un passager par son identifiant.
    // Retourne null si le passager n'existe pas.
    @Override
    public Passager chercherParId(Long id) {
        return passagerRepository.findById(id).orElse(null);
    }

    // Modifie les informations d'un passager existant.
    @Override
    public Passager modifier(Long id, Passager passager) {

        // Recherche le passager à modifier.
        Passager existant = chercherParId(id);

        // Vérifie si le passager existe.
        if (existant == null) {
            return null;
        }

        // Met à jour les informations du passager.
        existant.setNom(passager.getNom());
        existant.setPrenom(passager.getPrenom());
        existant.setEmail(passager.getEmail());
        existant.setTelephone(passager.getTelephone());

        // Enregistre les modifications dans la base de données.
        return passagerRepository.save(existant);
    }

    // Supprime un passager à partir de son identifiant.
    @Override
    public void supprimer(Long id) {

        // Recherche le passager à supprimer.
        Passager passager = chercherParId(id);

        // Vérifie que le passager existe avant de le supprimer.
        if (passager != null) {
            passagerRepository.delete(passager);
        }
    }
}