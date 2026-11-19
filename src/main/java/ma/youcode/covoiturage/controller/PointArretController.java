
package ma.youcode.covoiturage.controller;

import jakarta.validation.Valid;
import ma.youcode.covoiturage.dto.PointArretDTO;
import ma.youcode.covoiturage.entity.PointArret;
import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.mapper.PointArretMapper;
import ma.youcode.covoiturage.service.PointArretService;
import ma.youcode.covoiturage.service.TrajetService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/points-arret")
public class PointArretController {

    private final PointArretService pointArretService;
    private final TrajetService trajetService;
    private final PointArretMapper pointArretMapper;

    public PointArretController(
            PointArretService pointArretService,
            TrajetService trajetService,
            PointArretMapper pointArretMapper) {
        this.pointArretService = pointArretService;
        this.trajetService = trajetService;
        this.pointArretMapper = pointArretMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PointArretDTO creer(@Valid @RequestBody PointArretDTO dto) {
        Trajet trajet = trajetService.chercherParId(dto.trajetId());

        if (trajet == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Trajet introuvable");
        }

        PointArret pointArret = pointArretMapper.toEntity(dto, trajet);

        return pointArretMapper.toDTO(pointArretService.creer(pointArret));
    }

    @GetMapping
    public List<PointArretDTO> lister() {
        return pointArretService.lister()
                .stream()
                .map(pointArretMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public PointArretDTO chercherParId(@PathVariable Long id) {
        PointArret pointArret = pointArretService.chercherParId(id);

        if (pointArret == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Point d'arrêt introuvable");
        }

        return pointArretMapper.toDTO(pointArret);
    }

    @PutMapping("/{id}")
    public PointArretDTO modifier(
            @PathVariable Long id,
            @Valid @RequestBody PointArretDTO dto) {

        Trajet trajet = trajetService.chercherParId(dto.trajetId());

        if (trajet == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Trajet introuvable");
        }

        PointArret pointArret = pointArretMapper.toEntity(dto, trajet);
        PointArret modifie = pointArretService.modifier(id, pointArret);

        if (modifie == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Point d'arrêt introuvable");
        }

        return pointArretMapper.toDTO(modifie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        PointArret pointArret = pointArretService.chercherParId(id);

        if (pointArret == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Point d'arrêt introuvable");
        }

        pointArretService.supprimer(id);
    }
}
