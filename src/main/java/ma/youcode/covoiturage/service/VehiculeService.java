package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.entity.Vehicule;
import java.util.List;

public interface VehiculeService {

    Vehicule creer(Vehicule vehicule);

    List<Vehicule> lister();

    Vehicule chercherParId(Long id);

    Vehicule modifier(Long id, Vehicule vehicule);

    void supprimer(Long id);
}