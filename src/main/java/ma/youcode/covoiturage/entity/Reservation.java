//Cette entité représente une réservation effectuée par un passager sur un trajet
// . Elle contient les informations du réservation, notamment le nombre de places, le montant total, la date et le statut. Elle possède également plusieurs relations avec les autres entités : Passager, Trajet et deux Points d'arrêt, qui permettent de
// définir le point de montée et le point de descente du passager. »


package ma.youcode.covoiturage.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
// Indique que cette classe est une entité JPA liée à la base de données.

@Table(name = "reservations")
// Associe cette classe à la table reservations.

public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Identifiant unique généré automatiquement par la base de données.
    private Long id;

    // Nombre de places réservées par le passager.
    private int nombrePlaces;

    // Montant total à payer pour la réservation.
    // BigDecimal permet de gérer les valeurs monétaires avec précision.
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal montantTotal;

    // Date et heure auxquelles la réservation a été effectuée.
    private LocalDateTime dateReservation;

    @Enumerated(EnumType.STRING)
    // Enregistre le statut sous forme de texte (CONFIRMEE ou ANNULEE).
    private StatutReservation statut;

    @ManyToOne
    // Plusieurs réservations peuvent appartenir au même passager.
    @JoinColumn(name = "passager_id", nullable = false)
    // Crée une clé étrangère vers le passager.
    // Cette relation est obligatoire.
    private Passager passager;

    @ManyToOne
    // Plusieurs réservations peuvent concerner le même trajet.
    @JoinColumn(name = "trajet_id", nullable = false)
    // Crée une clé étrangère vers le trajet.
    private Trajet trajet;

    @ManyToOne
    // Plusieurs réservations peuvent utiliser le même point de montée.
    @JoinColumn(name = "point_montee_id", nullable = false)
    // Identifie le point où le passager monte dans le véhicule.
    private PointArret pointMontee;

    @ManyToOne
    // Plusieurs réservations peuvent utiliser le même point de descente.
    @JoinColumn(name = "point_descente_id", nullable = false)
    // Identifie le point où le passager descend du véhicule.
    private PointArret pointDescente;

    // Constructeur vide nécessaire pour JPA/Hibernate.
    public Reservation() {
    }

    // Getter : permet de récupérer l'identifiant.
    public Long getId() {
        return id;
    }

    // Getter et setter du nombre de places.
    public int getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(int nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    // Getter et setter du montant total.
    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    // Getter et setter de la date de réservation.
    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    // Getter et setter du statut de la réservation.
    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    // Getter et setter du passager associé à la réservation.
    public Passager getPassager() {
        return passager;
    }

    public void setPassager(Passager passager) {
        this.passager = passager;
    }

    // Getter et setter du trajet réservé.
    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    // Getter et setter du point de montée.
    public PointArret getPointMontee() {
        return pointMontee;
    }

    public void setPointMontee(PointArret pointMontee) {
        this.pointMontee = pointMontee;
    }

    // Getter et setter du point de descente.
    public PointArret getPointDescente() {
        return pointDescente;
    }

    public void setPointDescente(PointArret pointDescente) {
        this.pointDescente = pointDescente;
    }
}
