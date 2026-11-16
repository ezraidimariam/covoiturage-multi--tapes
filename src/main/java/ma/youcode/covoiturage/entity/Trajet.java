
package ma.youcode.covoiturage.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trajets")
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String villeDepart;

    private String villeArrivee;

    private LocalDateTime dateDepart;

    private int placesProposees;

    @Enumerated(EnumType.STRING)
    private StatutTrajet statut;

    @ManyToOne
    @JoinColumn(name = "conducteur_id", nullable = false)
    private Conducteur conducteur;

    @ManyToOne
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    @OneToMany(mappedBy = "trajet",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<PointArret> pointsArret = new ArrayList<>();

    @OneToMany(mappedBy = "trajet")
    private List<Reservation> reservations = new ArrayList<>();

    public Trajet() {
    }

    public Long getId() {
        return id;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public LocalDateTime getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }

    public int getPlacesProposees() {
        return placesProposees;
    }

    public void setPlacesProposees(int placesProposees) {
        this.placesProposees = placesProposees;
    }

    public StatutTrajet getStatut() {
        return statut;
    }

    public void setStatut(StatutTrajet statut) {
        this.statut = statut;
    }

    public Conducteur getConducteur() {
        return conducteur;
    }

    public void setConducteur(Conducteur conducteur) {
        this.conducteur = conducteur;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public List<PointArret> getPointsArret() {
        return pointsArret;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}
