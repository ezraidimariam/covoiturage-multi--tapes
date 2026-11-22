//L'entité Trajet représente un trajet proposé par un conducteur.
// Elle contient les informations principales comme la ville de départ, la ville d'arrivée, la date de départ, le nombre de places proposées et le statut. Elle est associée à un conducteur et à un véhicule. Elle possède également une liste de points d'arrêt et une liste de réservations,
// car notre plateforme gère les trajets multi-étapes.


package ma.youcode.covoiturage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
// Cette classe représente un trajet dans notre plateforme.

@Table(name = "trajets")
// Associe cette classe à la table trajets dans la base de données.

public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Identifiant unique généré automatiquement par la base de données.
    private Long id;

    // Ville de départ du trajet.
    private String villeDepart;

    // Ville d'arrivée du trajet.
    private String villeArrivee;

    // Date et heure de départ du trajet.
    private LocalDateTime dateDepart;

    // Nombre de places proposées par le conducteur.
    private int placesProposees;

    @Enumerated(EnumType.STRING)
    // Enregistre le statut du trajet sous forme de texte.
    // Exemple : PUBLIE, COMPLET, TERMINE ou ANNULE.
    private StatutTrajet statut;

    @ManyToOne
    // Plusieurs trajets peuvent être proposés par le même conducteur.
    @JoinColumn(name = "conducteur_id", nullable = false)
    // Clé étrangère qui relie le trajet à son conducteur.
    private Conducteur conducteur;

    @ManyToOne
    // Plusieurs trajets peuvent utiliser le même véhicule.
    @JoinColumn(name = "vehicule_id", nullable = false)
    // Clé étrangère qui relie le trajet à son véhicule.
    private Vehicule vehicule;

    @JsonManagedReference("trajet-points")
    // Permet d'inclure les points d'arrêt dans le JSON du trajet.
    // Fonctionne avec @JsonBackReference dans PointArret pour éviter
    // une boucle infinie lors de la conversion en JSON.

    @OneToMany(mappedBy = "trajet",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    // Un trajet peut avoir plusieurs points d'arrêt.
    // mappedBy indique que la relation est gérée par l'attribut
    // "trajet" dans la classe PointArret.
    // CascadeType.ALL propage les opérations de persistance
    // du trajet vers ses points d'arrêt.
    // orphanRemoval = true supprime un point d'arrêt retiré de la liste.
    private List<PointArret> pointsArret = new ArrayList<>();

    @JsonIgnore
    // Empêche l'affichage de la liste des réservations dans le JSON du trajet.

    @OneToMany(mappedBy = "trajet")
    // Un trajet peut avoir plusieurs réservations.
    // La relation est liée à l'attribut "trajet" dans Reservation.
    private List<Reservation> reservations = new ArrayList<>();

    // Constructeur vide nécessaire pour JPA/Hibernate.
    public Trajet() {
    }

    // Getter : permet de récupérer l'identifiant du trajet.
    public Long getId() {
        return id;
    }

    // Getter et setter de la ville de départ.
    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    // Getter et setter de la ville d'arrivée.
    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    // Getter et setter de la date de départ.
    public LocalDateTime getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }

    // Getter et setter du nombre de places proposées.
    public int getPlacesProposees() {
        return placesProposees;
    }

    public void setPlacesProposees(int placesProposees) {
        this.placesProposees = placesProposees;
    }

    // Getter et setter du statut du trajet.
    public StatutTrajet getStatut() {
        return statut;
    }

    public void setStatut(StatutTrajet statut) {
        this.statut = statut;
    }

    // Getter et setter du conducteur associé au trajet.
    public Conducteur getConducteur() {
        return conducteur;
    }

    public void setConducteur(Conducteur conducteur) {
        this.conducteur = conducteur;
    }

    // Getter et setter du véhicule utilisé pour le trajet.
    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    // Getter : retourne la liste des points d'arrêt du trajet.
    public List<PointArret> getPointsArret() {
        return pointsArret;
    }

    // Getter : retourne la liste des réservations du trajet.
    public List<Reservation> getReservations() {
        return reservations;
    }
}
