
package ma.youcode.covoiturage.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RechercheTrajetDTO(

        @NotBlank
        String villeDepart,

        @NotBlank
        String villeArrivee,

        @NotNull
        LocalDate dateDepart,

        @Min(1)
        int nombrePlaces,

        BigDecimal prixMax

) {
}
