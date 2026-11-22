//Cette entité représente un point d'arrêt d'un trajet. Elle contient la ville, l'ordre de passage, l'heure prévue et le prix depuis le départ.
// Elle possède également une relation Many-to-One avec Trajet, car
// plusieurs points d'arrêt peuvent appartenir au même trajet.

package ma.youcode.covoiturage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Cette classe représente un point d'arrêt dans un trajet.
@Entity
@Table(name = "points_arret")
public class PointArret {

    // Identifiant unique généré automatiquement.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ville où se trouve le point d'arrêt.
    private String ville;

    // Position du point d'arrêt dans l'ordre du trajet.
    private int ordre;

    // Date et heure prévues du passage.
    private LocalDateTime heurePassage;

    // Prix calculé depuis le point de départ jusqu'à cet arrêt.
    private BigDecimal prixDepuisDepart;

    // Évite la récursion infinie lors de la conversion en JSON.
    @JsonBackReference("trajet-points")

    // Plusieurs points d'arrêt peuvent appartenir à un même trajet.
    @ManyToOne

    // Clé étrangère qui relie le point d'arrêt à son trajet.
    // Chaque point d'arrêt doit obligatoirement avoir un trajet.
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    // Constructeur vide nécessaire pour JPA/Hibernate.
    public PointArret() {
    }

    // Retourne l'identifiant du point d'arrêt.
    public Long getId() {
        return id;
    }

    // Retourne la ville.
    public String getVille() {
        return ville;
    }

    // Modifie la ville.
    public void setVille(String ville) {
        this.ville = ville;
    }

    // Retourne l'ordre du point d'arrêt.
    public int getOrdre() {
        return ordre;
    }

    // Modifie l'ordre du point d'arrêt.
    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    // Retourne la date et l'heure de passage.
    public LocalDateTime getHeurePassage() {
        return heurePassage;
    }

    // Modifie la date et l'heure de passage.
    public void setHeurePassage(LocalDateTime heurePassage) {
        this.heurePassage = heurePassage;
    }

    // Retourne le prix depuis le départ.
    public BigDecimal getPrixDepuisDepart() {
        return prixDepuisDepart;
    }

    // Modifie le prix depuis le départ.
    public void setPrixDepuisDepart(BigDecimal prixDepuisDepart) {
        this.prixDepuisDepart = prixDepuisDepart;
    }

    // Retourne le trajet associé.
    public Trajet getTrajet() {
        return trajet;
    }

    // Associe ce point d'arrêt à un trajet.
    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }
}