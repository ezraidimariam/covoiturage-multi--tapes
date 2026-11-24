
package ma.youcode.covoiturage.controller;

import jakarta.validation.Valid;
import ma.youcode.covoiturage.dto.VehiculeDTO;
import ma.youcode.covoiturage.entity.Conducteur;
import ma.youcode.covoiturage.entity.Vehicule;
import ma.youcode.covoiturage.mapper.VehiculeMapper;
import ma.youcode.covoiturage.service.ConducteurService;
import ma.youcode.covoiturage.service.VehiculeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
public class VehiculeController {

    private final VehiculeService vehiculeService;
    private final ConducteurService conducteurService;
    private final VehiculeMapper vehiculeMapper;

    public VehiculeController(
            VehiculeService vehiculeService,
            ConducteurService conducteurService,
            VehiculeMapper vehiculeMapper) {
        this.vehiculeService = vehiculeService;
        this.conducteurService = conducteurService;
        this.vehiculeMapper = vehiculeMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculeDTO creer(@Valid @RequestBody VehiculeDTO vehiculeDTO) {

        Conducteur conducteur = conducteurService.chercherParId(vehiculeDTO.conducteurId());

        if (conducteur == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conducteur introuvable");
        }

        Vehicule vehicule = vehiculeMapper.toEntity(vehiculeDTO, conducteur);

        return vehiculeMapper.toDTO(vehiculeService.creer(vehicule));
    }

    @GetMapping
    public List<VehiculeDTO> lister() {
        return vehiculeService.lister()
                .stream()
                .map(vehiculeMapper::toDTO)
                .toList();
    }

    @GetMapping("/conducteur/{conducteurId}")
    public List<VehiculeDTO> trouverParConducteur(@PathVariable Long conducteurId) {
        if (conducteurService.chercherParId(conducteurId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conducteur introuvable");
        }
        return vehiculeService.trouverParConducteur(conducteurId).stream()
                .map(vehiculeMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public VehiculeDTO chercherParId(@PathVariable Long id) {

        Vehicule vehicule = vehiculeService.chercherParId(id);

        if (vehicule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicule introuvable");
        }

        return vehiculeMapper.toDTO(vehicule);
    }

    @PutMapping("/{id}")
    public VehiculeDTO modifier(
            @PathVariable Long id,
            @Valid @RequestBody VehiculeDTO vehiculeDTO) {

        Conducteur conducteur = conducteurService.chercherParId(vehiculeDTO.conducteurId());

        if (conducteur == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conducteur introuvable");
        }

        Vehicule vehicule = vehiculeMapper.toEntity(vehiculeDTO, conducteur);
        Vehicule modifie = vehiculeService.modifier(id, vehicule);

        if (modifie == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicule introuvable");
        }

        return vehiculeMapper.toDTO(modifie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        vehiculeService.supprimer(id);
    }
}
