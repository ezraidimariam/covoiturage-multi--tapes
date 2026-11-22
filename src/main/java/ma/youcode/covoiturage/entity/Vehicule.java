//L'entité Vehicule représente les véhicules enregistrés par les
// conducteurs. Elle contient les informations du véhicule, notamment sa marque, son modèle, son immatriculation et sa capacité. Chaque véhicule est associé à un seul conducteur
// , tandis qu'un conducteur peut posséder plusieurs véhicules. »


package ma.youcode.covoiturage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

// Indique que cette classe représente une entité JPA.
@Entity
// Définit le nom de la table dans la base de données.
@Table(name = "vehicules")
public class Vehicule {

    // Identifiant unique généré automatiquement.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Marque du véhicule.
    private String marque;

    // Modèle du véhicule.
    private String modele;

    // Immatriculation obligatoire et unique.
    @Column(nullable = false, unique = true)
    private String immatriculation;

    // Couleur du véhicule.
    private String couleur;

    // La capacité doit être comprise entre 1 et 8 places.
    @Min(1)
    @Max(8)
    @Column(nullable = false)
    private int capacite;

    // Plusieurs véhicules peuvent appartenir au même conducteur.
    @ManyToOne
    // Définit la clé étrangère vers le conducteur.
    @JoinColumn(name = "conducteur_id", nullable = false)
    private Conducteur conducteur;

    // Constructeur vide nécessaire à JPA.
    public Vehicule() {
    }

    // Retourne l'identifiant du véhicule.
    public Long getId() {
        return id;
    }

    // Retourne la marque du véhicule.
    public String getMarque() {
        return marque;
    }

    // Modifie la marque du véhicule.
    public void setMarque(String marque) {
        this.marque = marque;
    }

    // Retourne le modèle du véhicule.
    public String getModele() {
        return modele;
    }

    // Modifie le modèle du véhicule.
    public void setModele(String modele) {
        this.modele = modele;
    }

    // Retourne l'immatriculation du véhicule.
    public String getImmatriculation() {
        return immatriculation;
    }

    // Modifie l'immatriculation du véhicule.
    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    // Retourne la couleur du véhicule.
    public String getCouleur() {
        return couleur;
    }

    // Modifie la couleur du véhicule.
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    // Retourne la capacité du véhicule.
    public int getCapacite() {
        return capacite;
    }

    // Modifie la capacité du véhicule.
    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    // Retourne le conducteur propriétaire du véhicule.
    public Conducteur getConducteur() {
        return conducteur;
    }

    // Associe un conducteur au véhicule.
    public void setConducteur(Conducteur conducteur) {
        this.conducteur = conducteur;
    }
}
