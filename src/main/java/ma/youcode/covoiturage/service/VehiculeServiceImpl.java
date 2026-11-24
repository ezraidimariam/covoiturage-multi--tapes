
package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.entity.Conducteur;
import ma.youcode.covoiturage.entity.Vehicule;
import ma.youcode.covoiturage.repository.TrajetRepository;
import ma.youcode.covoiturage.repository.VehiculeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final TrajetRepository trajetRepository;

    public VehiculeServiceImpl(
            VehiculeRepository vehiculeRepository,
            TrajetRepository trajetRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.trajetRepository = trajetRepository;
    }

    @Override
    public Vehicule creer(Vehicule vehicule) {

        if (vehiculeRepository.existsByImmatriculation(vehicule.getImmatriculation())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cette immatriculation existe déjà"
            );
        }

        return vehiculeRepository.save(vehicule);
    }

    @Override
    public List<Vehicule> lister() {
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule chercherParId(Long id) {
        return vehiculeRepository.findById(id).orElse(null);
    }

    @Override
    public Vehicule modifier(Long id, Vehicule vehicule) {

        Vehicule existant = chercherParId(id);

        if (existant == null) {
            return null;
        }

        if (vehiculeRepository.existsByImmatriculationAndIdNot(
                vehicule.getImmatriculation(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cette immatriculation existe déjà"
            );
        }

        existant.setMarque(vehicule.getMarque());
        existant.setModele(vehicule.getModele());
        existant.setImmatriculation(vehicule.getImmatriculation());
        existant.setCouleur(vehicule.getCouleur());
        existant.setCapacite(vehicule.getCapacite());
        existant.setConducteur(vehicule.getConducteur());

        return vehiculeRepository.save(existant);
    }

    @Override
    public void supprimer(Long id) {

        Vehicule vehicule = chercherParId(id);

        if (vehicule == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Vehicule introuvable"
            );
        }

        if (trajetRepository.existsByVehicule_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Impossible de supprimer un véhicule utilisé dans un trajet"
            );
        }

        vehiculeRepository.delete(vehicule);
    }
}
