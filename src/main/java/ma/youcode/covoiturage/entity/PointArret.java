
package ma.youcode.covoiturage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "points_arret")
public class PointArret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ville;

    private int ordre;

    private LocalDateTime heurePassage;

    private BigDecimal prixDepuisDepart;

    @JsonBackReference("trajet-points")
    @ManyToOne
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    public PointArret() {
    }

    public Long getId() {
        return id;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public LocalDateTime getHeurePassage() {
        return heurePassage;
    }

    public void setHeurePassage(LocalDateTime heurePassage) {
        this.heurePassage = heurePassage;
    }

    public BigDecimal getPrixDepuisDepart() {
        return prixDepuisDepart;
    }

    public void setPrixDepuisDepart(BigDecimal prixDepuisDepart) {
        this.prixDepuisDepart = prixDepuisDepart;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }
}
