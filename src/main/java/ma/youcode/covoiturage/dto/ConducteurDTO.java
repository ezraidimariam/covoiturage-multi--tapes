package ma.youcode.covoiturage.dto;

public record ConducteurDTO(
        Long id,
        String nom,
        String prenom,
        String email,
        String telephone,
        String numeroPermis
) {
}