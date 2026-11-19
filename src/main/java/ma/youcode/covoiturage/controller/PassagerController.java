
package ma.youcode.covoiturage.controller;

import jakarta.validation.Valid;
import ma.youcode.covoiturage.dto.PassagerDTO;
import ma.youcode.covoiturage.entity.Passager;
import ma.youcode.covoiturage.mapper.PassagerMapper;
import ma.youcode.covoiturage.service.PassagerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/passagers")
public class PassagerController {

    private final PassagerService passagerService;
    private final PassagerMapper passagerMapper;

    public PassagerController(PassagerService passagerService,
                              PassagerMapper passagerMapper) {
        this.passagerService = passagerService;
        this.passagerMapper = passagerMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PassagerDTO creer(@Valid @RequestBody PassagerDTO passagerDTO) {
        Passager passager = passagerMapper.toEntity(passagerDTO);
        return passagerMapper.toDTO(passagerService.creer(passager));
    }

    @GetMapping
    public List<PassagerDTO> lister() {
        return passagerService.lister()
                .stream()
                .map(passagerMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public PassagerDTO chercherParId(@PathVariable Long id) {
        Passager passager = passagerService.chercherParId(id);

        if (passager == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Passager introuvable");
        }

        return passagerMapper.toDTO(passager);
    }

    @PutMapping("/{id}")
    public PassagerDTO modifier(
            @PathVariable Long id,
            @Valid @RequestBody PassagerDTO passagerDTO) {

        Passager passager = passagerMapper.toEntity(passagerDTO);
        Passager modifie = passagerService.modifier(id, passager);

        if (modifie == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Passager introuvable");
        }

        return passagerMapper.toDTO(modifie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        Passager passager = passagerService.chercherParId(id);

        if (passager == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Passager introuvable");
        }

        passagerService.supprimer(id);
    }
}
