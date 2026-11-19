
package ma.youcode.covoiturage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PassagerDTO(
        Long id,

        @NotBlank
        String nom,

        @NotBlank
        String prenom,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String telephone
) {}
