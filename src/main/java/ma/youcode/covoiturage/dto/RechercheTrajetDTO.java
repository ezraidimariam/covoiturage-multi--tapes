
package ma.youcode.covoiturage.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

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

        @DecimalMin(value = "0.00")
        BigDecimal prixMax

) {
}
