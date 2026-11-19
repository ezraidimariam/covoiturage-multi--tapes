package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.ReservationDTO;

import java.util.List;

public interface ReservationService {

    ReservationDTO creer(ReservationDTO dto);

    List<ReservationDTO> trouverTous();

    ReservationDTO trouverParId(Long id);

    ReservationDTO modifier(Long id, ReservationDTO dto);

    void supprimer(Long id);
}