package ma.youcode.covoiturage.dto;

import ma.youcode.covoiturage.entity.StatutTrajet;
import java.time.LocalDateTime;
import java.util.List;

public record TrajetDetailDTO(
        Long id,
        String villeDepart,
        String villeArrivee,
        LocalDateTime dateDepart,
        int placesProposees,
        long placesRestantes,
        StatutTrajet statut,
        Long conducteurId,
        String conducteurNom,
        Long vehiculeId,
        List<PointArretDTO> pointsArret
) {}
