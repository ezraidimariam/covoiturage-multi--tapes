
package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.entity.PointArret;
import ma.youcode.covoiturage.entity.StatutTrajet;
import ma.youcode.covoiturage.repository.PointArretRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PointArretServiceImpl implements PointArretService {

    private final PointArretRepository pointArretRepository;

    public PointArretServiceImpl(PointArretRepository pointArretRepository) {
        this.pointArretRepository = pointArretRepository;
    }

    @Override
    public PointArret creer(PointArret pointArret) {

        verifierTrajetModifiable(pointArret);

        verifierPoint(pointArret, null);

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

        if (!existant.getTrajet().getId()
                .equals(pointArret.getTrajet().getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Un point d'arrêt ne peut pas changer de trajet"
            );
        }

        verifierTrajetModifiable(existant);

        verifierPoint(pointArret, id);

        existant.setVille(pointArret.getVille());
        existant.setOrdre(pointArret.getOrdre());
        existant.setHeurePassage(pointArret.getHeurePassage());
        existant.setPrixDepuisDepart(pointArret.getPrixDepuisDepart());

        return pointArretRepository.save(existant);
    }

    @Override
    public void supprimer(Long id) {

        PointArret pointArret = chercherParId(id);

        if (pointArret != null) {

            verifierTrajetModifiable(pointArret);

            pointArretRepository.delete(pointArret);
        }
    }

    private void verifierTrajetModifiable(PointArret point) {

        StatutTrajet statut = point.getTrajet().getStatut();

        if (statut != null) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Les points d'arrêt ne peuvent être modifiés qu'avant publication"
            );
        }
    }

    private void verifierPoint(PointArret point, Long idIgnore) {

        List<PointArret> points = new ArrayList<>(
                pointArretRepository.findAll()
                        .stream()
                        .filter(p -> p.getTrajet().getId()
                                .equals(point.getTrajet().getId()))
                        .filter(p -> idIgnore == null
                                || !p.getId().equals(idIgnore))
                        .toList()
        );

        for (PointArret autre : points) {

            if (autre.getVille().equalsIgnoreCase(point.getVille())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cette ville existe déjà dans l'itinéraire"
                );
            }

            if (autre.getOrdre() == point.getOrdre()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cet ordre existe déjà dans l'itinéraire"
                );
            }
        }

        if (point.getOrdre() == 0
                && point.getPrixDepuisDepart()
                .compareTo(BigDecimal.ZERO) != 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le prix du premier point doit être égal à 0"
            );
        }

        points.add(point);

        points.sort(Comparator.comparingInt(PointArret::getOrdre));

        for (int i = 1; i < points.size(); i++) {

            PointArret precedent = points.get(i - 1);
            PointArret actuel = points.get(i);

            if (!actuel.getHeurePassage()
                    .isAfter(precedent.getHeurePassage())) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Les heures de passage doivent être croissantes"
                );
            }

            if (actuel.getPrixDepuisDepart()
                    .compareTo(precedent.getPrixDepuisDepart()) <= 0) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Les prix doivent être strictement croissants"
                );
            }
        }
    }
}
