package ma.youcode.covoiturage.controller;

import jakarta.validation.Valid;
import ma.youcode.covoiturage.dto.ReservationDTO;
import ma.youcode.covoiturage.service.ReservationService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ReservationDTO creer(@Valid @RequestBody ReservationDTO dto) {
        return reservationService.creer(dto);
    }

    @GetMapping
    public List<ReservationDTO> trouverTous() {
        return reservationService.trouverTous();
    }

    @GetMapping("/{id}")
    public ReservationDTO trouverParId(@PathVariable Long id) {
        return reservationService.trouverParId(id);
    }

    @PutMapping("/{id}")
    public ReservationDTO modifier(
            @PathVariable Long id,
            @Valid @RequestBody ReservationDTO dto
    ) {
        return reservationService.modifier(id, dto);
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        reservationService.supprimer(id);
    }
}