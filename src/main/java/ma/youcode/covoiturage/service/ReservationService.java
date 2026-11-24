
package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.ReservationDTO;

import java.util.List;

public interface ReservationService {

    ReservationDTO creer(ReservationDTO dto);

    List<ReservationDTO> trouverTous();

    List<ReservationDTO> trouverParPassager(Long passagerId);

    List<ReservationDTO> trouverParConducteur(Long conducteurId);

    ReservationDTO trouverParId(Long id);

    ReservationDTO modifier(Long id, ReservationDTO dto);


    void annuler(Long id);

    void supprimer(Long id);
}
