
package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.repository.TrajetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrajetServiceImpl implements TrajetService {

    private final TrajetRepository trajetRepository;

    public TrajetServiceImpl(TrajetRepository trajetRepository) {
        this.trajetRepository = trajetRepository;
    }

    @Override
    public Trajet creer(Trajet trajet) {
        return trajetRepository.save(trajet);
    }

    @Override
    public List<Trajet> lister() {
        return trajetRepository.findAll();
    }

    @Override
    public Trajet chercherParId(Long id) {
        return trajetRepository.findById(id).orElse(null);
    }

    @Override
    public Trajet modifier(Long id, Trajet trajet) {

        Trajet existant = chercherParId(id);

        if (existant == null) {
            return null;
        }

        existant.setVilleDepart(trajet.getVilleDepart());
        existant.setVilleArrivee(trajet.getVilleArrivee());
        existant.setDateDepart(trajet.getDateDepart());
        existant.setPlacesProposees(trajet.getPlacesProposees());
        existant.setStatut(trajet.getStatut());
        existant.setConducteur(trajet.getConducteur());
        existant.setVehicule(trajet.getVehicule());

        return trajetRepository.save(existant);
    }

    @Override
    public void supprimer(Long id) {

        Trajet trajet = chercherParId(id);

        if (trajet != null) {
            trajetRepository.delete(trajet);
        }
    }
}
