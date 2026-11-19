
package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.entity.PointArret;
import java.util.List;

public interface PointArretService {

    PointArret creer(PointArret pointArret);

    List<PointArret> lister();

    PointArret chercherParId(Long id);

    PointArret modifier(Long id, PointArret pointArret);

    void supprimer(Long id);
}
