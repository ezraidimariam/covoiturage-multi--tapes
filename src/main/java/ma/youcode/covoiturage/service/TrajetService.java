
package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.entity.Trajet;
import java.util.List;

public interface TrajetService {

    Trajet creer(Trajet trajet);

    List<Trajet> lister();

    Trajet chercherParId(Long id);

    Trajet modifier(Long id, Trajet trajet);

    void supprimer(Long id);
}
