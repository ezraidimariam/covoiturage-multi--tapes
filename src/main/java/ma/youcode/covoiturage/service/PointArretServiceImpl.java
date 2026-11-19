
package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.entity.PointArret;
import ma.youcode.covoiturage.repository.PointArretRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointArretServiceImpl implements PointArretService {

    private final PointArretRepository pointArretRepository;

    public PointArretServiceImpl(PointArretRepository pointArretRepository) {
        this.pointArretRepository = pointArretRepository;
    }

    @Override
    public PointArret creer(PointArret pointArret) {
        return pointArretRepository.save(pointArret);
    }

    @Override
    public List<PointArret> lister() {
        return pointArretRepository.findAll();
    }

    @Override
    public PointArret chercherParId(Long id) {
        return pointArretRepository.findById(id).orElse(null);
    }

    @Override
    public PointArret modifier(Long id, PointArret pointArret) {
        PointArret existant = chercherParId(id);

        if (existant == null) {
            return null;
        }

        existant.setVille(pointArret.getVille());
        existant.setOrdre(pointArret.getOrdre());
        existant.setHeurePassage(pointArret.getHeurePassage());
        existant.setPrixDepuisDepart(pointArret.getPrixDepuisDepart());
        existant.setTrajet(pointArret.getTrajet());

        return pointArretRepository.save(existant);
    }

    @Override
    public void supprimer(Long id) {
        PointArret pointArret = chercherParId(id);

        if (pointArret != null) {
            pointArretRepository.delete(pointArret);
        }
    }
}
