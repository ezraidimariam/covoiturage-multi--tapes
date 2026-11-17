package ma.youcode.covoiturage.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehiculeDTO(
        Long id,

        @NotBlank
        String marque,

        @NotBlank
        String modele,

        @NotBlank
        String immatriculation,

        @NotBlank
        String couleur,

        @Min(1)
        @Max(8)
        int capacite,

        @NotNull
        Long conducteurId
) {
}