package ma.youcode.covoiturage.dto;

import ma.youcode.covoiturage.entity.Trajet;
import java.math.BigDecimal;

public record TrajetRechercheResultatDTO(
        Trajet trajet,
        BigDecimal prixSegment,
        Long placesRestantes
) {
    public TrajetRechercheResultatDTO(Trajet trajet, int prixSegment, int placesRestantes) {
        this(trajet, BigDecimal.valueOf(prixSegment), (long) placesRestantes);
    }
}