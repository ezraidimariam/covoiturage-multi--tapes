
package ma.youcode.covoiturage.controller;

import jakarta.validation.Valid;
import ma.youcode.covoiturage.dto.TrajetDTO;
import ma.youcode.covoiturage.entity.Conducteur;
import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.entity.Vehicule;
import ma.youcode.covoiturage.mapper.TrajetMapper;
import ma.youcode.covoiturage.service.ConducteurService;
import ma.youcode.covoiturage.service.TrajetService;
import ma.youcode.covoiturage.service.VehiculeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/trajets")
public class TrajetController {

    private final TrajetService trajetService;
    private final ConducteurService conducteurService;
    private final VehiculeService vehiculeService;
    private final TrajetMapper trajetMapper;

    public TrajetController(
            TrajetService trajetService,
            ConducteurService conducteurService,
            VehiculeService vehiculeService,
            TrajetMapper trajetMapper) {
        this.trajetService = trajetService;
        this.conducteurService = conducteurService;
        this.vehiculeService = vehiculeService;
        this.trajetMapper = trajetMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrajetDTO creer(@Valid @RequestBody TrajetDTO trajetDTO) {

        Conducteur conducteur =
                conducteurService.chercherParId(trajetDTO.conducteurId());

        if (conducteur == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Conducteur introuvable");
        }

        Vehicule vehicule =
                vehiculeService.chercherParId(trajetDTO.vehiculeId());

        if (vehicule == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Vehicule introuvable");
        }

        if (!vehicule.getConducteur().getId().equals(conducteur.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Ce véhicule n'appartient pas à ce conducteur");
        }

        if (trajetDTO.placesProposees() > vehicule.getCapacite()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le nombre de places dépasse la capacité du véhicule");
        }

        Trajet trajet = trajetMapper.toEntity(trajetDTO, conducteur, vehicule);

        return trajetMapper.toDTO(trajetService.creer(trajet));
    }

    @GetMapping
    public List<TrajetDTO> lister() {
        return trajetService.lister()
                .stream()
                .map(trajetMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public TrajetDTO chercherParId(@PathVariable Long id) {
        Trajet trajet = trajetService.chercherParId(id);
        return trajetMapper.toDTO(trajet);
    }

    @PutMapping("/{id}")
    public TrajetDTO modifier(
            @PathVariable Long id,
            @Valid @RequestBody TrajetDTO trajetDTO) {

        Conducteur conducteur =
                conducteurService.chercherParId(trajetDTO.conducteurId());

        if (conducteur == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Conducteur introuvable");
        }

        Vehicule vehicule =
                vehiculeService.chercherParId(trajetDTO.vehiculeId());

        if (vehicule == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Vehicule introuvable");
        }

        if (!vehicule.getConducteur().getId().equals(conducteur.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Ce véhicule n'appartient pas à ce conducteur");
        }

        if (trajetDTO.placesProposees() > vehicule.getCapacite()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le nombre de places dépasse la capacité du véhicule");
        }

        Trajet trajet = trajetMapper.toEntity(trajetDTO, conducteur, vehicule);
        Trajet modifie = trajetService.modifier(id, trajet);

        if (modifie == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Trajet introuvable");
        }

        return trajetMapper.toDTO(modifie);
    }

    @PostMapping("/{id}/publier")
    public TrajetDTO publier(@PathVariable Long id) {
        return trajetMapper.toDTO(trajetService.publier(id));
    }

    @PostMapping("/{id}/annuler")
    public TrajetDTO annuler(@PathVariable Long id) {
        trajetService.annuler(id);
        return trajetMapper.toDTO(trajetService.chercherParId(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        trajetService.supprimer(id);
    }
}
