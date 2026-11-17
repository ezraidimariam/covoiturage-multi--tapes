package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.entity.Conducteur;

import java.util.List;

public interface ConducteurService {

    Conducteur creer(Conducteur conducteur);

    List<Conducteur> lister();

    Conducteur chercherParId(Long id);

    Conducteur modifier(Long id, Conducteur conducteur);

    void supprimer(Long id);
}