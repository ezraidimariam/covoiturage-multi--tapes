//Ce fichier est une interface qui définit les opérations de gestion des passagers. Il déclare cinq méthodes : créer un passager, récupérer la liste des passagers, rechercher un passager par son identifiant, modifier ses informations et le supprimer. L'implémentation
// de ces méthodes se trouve dans la classe PassagerServiceImpl
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
