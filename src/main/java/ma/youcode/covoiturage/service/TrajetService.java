
package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.RechercheTrajetDTO;
import ma.youcode.covoiturage.dto.TrajetRechercheResultatDTO;
import ma.youcode.covoiturage.entity.Trajet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TrajetService {

    Trajet creer(Trajet trajet);

    List<Trajet> lister();

    Trajet chercherParId(Long id);

    long placesRestantes(Long id);

    Trajet modifier(Long id, Trajet trajet);

    Trajet publier(Long id);

    void annuler(Long id);

    Trajet terminer(Long id);

    void supprimer(Long id);

    Page<TrajetRechercheResultatDTO> rechercher(
            RechercheTrajetDTO criteres,
            Pageable pageable
    );
}
