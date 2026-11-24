
package ma.youcode.covoiturage.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PointArretDTO(
        Long id,

        @NotBlank
        String ville,

        @Min(0)
        int ordre,

        @NotNull
        LocalDateTime heurePassage,

        @NotNull
        @DecimalMin(value = "0.00")
        BigDecimal prixDepuisDepart,

        @NotNull
        Long trajetId
) {}
