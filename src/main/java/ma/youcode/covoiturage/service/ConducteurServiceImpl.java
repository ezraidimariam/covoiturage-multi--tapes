//Ce fichier représente la classe d'implémentation du service Conducteur
// . Il contient la logique métier liée à la gestion des conducteurs. Il permet de créer un conducteur, afficher la liste des conducteurs, rechercher un conducteur par son identifiant, modifier ses informations et le supprimer. Pour accéder à la base de données, cette classe utilise ConducteurRepository,
// injecté par constructeur grâce à Spring. »

package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.entity.Conducteur;
import ma.youcode.covoiturage.entity.Vehicule;
import ma.youcode.covoiturage.repository.ConducteurRepository;
import ma.youcode.covoiturage.repository.VehiculeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

// Indique à Spring que cette classe contient la logique métier des conducteurs.
@Service
public class ConducteurServiceImpl implements ConducteurService {

    // Repository utilisé pour accéder aux données des conducteurs.
    private final ConducteurRepository conducteurRepository;
    private final VehiculeRepository vehiculeRepository;

    // Injection du Repository par constructeur.
    public ConducteurServiceImpl(ConducteurRepository conducteurRepository, VehiculeRepository vehiculeRepository) {
        this.conducteurRepository = conducteurRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    // Crée un nouveau conducteur et l'enregistre dans la base de données.
    @Override
    public Conducteur creer(Conducteur conducteur) {
        return conducteurRepository.save(conducteur);
    }

    // Récupère la liste de tous les conducteurs.
    @Override
    public List<Conducteur> lister() {
        return conducteurRepository.findAll();
    }

    // Recherche un conducteur par son identifiant.
    // Retourne null si le conducteur n'existe pas.
    @Override
    public Conducteur chercherParId(Long id) {
        return conducteurRepository.findById(id).orElse(null);
    }

    // Modifie les informations d'un conducteur existant.
    @Override
    public Conducteur modifier(Long id, Conducteur conducteur) {

        // Recherche le conducteur à modifier.
        Conducteur existant = chercherParId(id);

        // Vérifie si le conducteur existe.
        if (existant == null) {
            return null;
        }

        // Met à jour les informations du conducteur.
        existant.setNom(conducteur.getNom());
        existant.setPrenom(conducteur.getPrenom());
        existant.setEmail(conducteur.getEmail());
        existant.setTelephone(conducteur.getTelephone());
        existant.setNumeroPermis(conducteur.getNumeroPermis());

        // Enregistre les modifications dans la base de données.
        return conducteurRepository.save(existant);
    }

    // Supprime un conducteur à partir de son identifiant.
    @Override
    public void supprimer(Long id) {
        conducteurRepository.deleteById(id);
    }
}
