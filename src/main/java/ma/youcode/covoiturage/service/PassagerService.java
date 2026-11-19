
package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.entity.Passager;
import java.util.List;

public interface PassagerService {

    Passager creer(Passager passager);

    List<Passager> lister();

    Passager chercherParId(Long id);

    Passager modifier(Long id, Passager passager);

    void supprimer(Long id);
}
