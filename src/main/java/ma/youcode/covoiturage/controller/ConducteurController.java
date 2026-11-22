
package ma.youcode.covoiturage.controller;

import ma.youcode.covoiturage.dto.ConducteurDTO;
import ma.youcode.covoiturage.entity.Conducteur;
import ma.youcode.covoiturage.mapper.ConducteurMapper;
import ma.youcode.covoiturage.service.ConducteurService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/conducteurs")
public class ConducteurController {

    private final ConducteurService conducteurService;
    private final ConducteurMapper conducteurMapper;

    public ConducteurController(
            ConducteurService conducteurService,
            ConducteurMapper conducteurMapper) {
        this.conducteurService = conducteurService;
        this.conducteurMapper = conducteurMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConducteurDTO creer(@RequestBody ConducteurDTO conducteurDTO) {
        Conducteur conducteur = conducteurMapper.toEntity(conducteurDTO);
        Conducteur nouveauConducteur = conducteurService.creer(conducteur);
        return conducteurMapper.toDTO(nouveauConducteur);
    }

    @GetMapping
    public List<ConducteurDTO> lister() {
        return conducteurService.lister()
                .stream()
                .map(conducteurMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ConducteurDTO chercherParId(@PathVariable Long id) {
        Conducteur conducteur = conducteurService.chercherParId(id);

        if (conducteur == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Conducteur introuvable"
            );
        }

        return conducteurMapper.toDTO(conducteur);
    }

    @PutMapping("/{id}")
    public ConducteurDTO modifier(
            @PathVariable Long id,
            @RequestBody ConducteurDTO conducteurDTO) {

        Conducteur conducteur = conducteurMapper.toEntity(conducteurDTO);
        Conducteur modifie = conducteurService.modifier(id, conducteur);

        if (modifie == null) {
            return null;
        }

        return conducteurMapper.toDTO(modifie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        conducteurService.supprimer(id);
    }
}
