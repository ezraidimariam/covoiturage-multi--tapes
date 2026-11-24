package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.entity.Vehicule;
import ma.youcode.covoiturage.entity.Conducteur;

import java.util.List;
import java.util.Map;

public interface VehiculeService {

    Vehicule creer(Vehicule vehicule);

    List<Vehicule> lister();

    Vehicule chercherParId(Long id);

    Vehicule modifier(Long id, Vehicule vehicule);

    void supprimer(Long id);
}