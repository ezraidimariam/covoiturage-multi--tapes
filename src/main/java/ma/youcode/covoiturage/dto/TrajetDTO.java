
package ma.youcode.covoiturage.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ma.youcode.covoiturage.entity.StatutTrajet;

import java.time.LocalDateTime;

public record TrajetDTO(

        Long id,

        @NotBlank
        String villeDepart,

        @NotBlank
        String villeArrivee,

        @NotNull
        LocalDateTime dateDepart,

        @Min(1)
        int placesProposees,

        StatutTrajet statut,

        @NotNull
        Long conducteurId,

        @NotNull
        Long vehiculeId

) {
}
