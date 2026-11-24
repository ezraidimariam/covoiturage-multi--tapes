
package ma.youcode.covoiturage.controller;

import jakarta.validation.Valid;
import ma.youcode.covoiturage.dto.ConducteurDTO;
import ma.youcode.covoiturage.entity.Conducteur;
import ma.youcode.covoiturage.mapper.ConducteurMapper;
import ma.youcode.covoiturage.service.ConducteurService;
import ma.youcode.covoiturage.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
// on a cree ce .. pour handler les requetes dun conducteur

@RestController
@RequestMapping("/api/conducteurs")
public class ConducteurController {

    private final ConducteurService conducteurService;
    private final ConducteurMapper conducteurMapper;
    private final ReservationService reservationService;
//CONSTRU OU ON AJOUTE ...
    public ConducteurController(
            ConducteurService conducteurService,
            ConducteurMapper conducteurMapper,
            ReservationService reservationService) {
        this.conducteurService = conducteurService;
        this.conducteurMapper = conducteurMapper;
        this.reservationService = reservationService;
    }

    @GetMapping("/rechercher")
    public List<ConducteurDTO> rechercher(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String email) {
        if ((nom == null || nom.isBlank()) && (email == null || email.isBlank())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Un nom ou un email doit être fourni"
            );
        }
        return conducteurService.rechercher(nom, email).stream()
                .map(conducteurMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}/reservations")
    public List<ma.youcode.covoiturage.dto.ReservationDTO> reservations(
            @PathVariable Long id) {
        if (conducteurService.chercherParId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conducteur introuvable");
        }
        return reservationService.trouverParConducteur(id);
    }

    @PostMapping//celui qui handle methode post
    @ResponseStatus(HttpStatus.CREATED)//
    public ConducteurDTO creer(@Valid @RequestBody ConducteurDTO conducteurDTO) {
        Conducteur conducteur = conducteurMapper.toEntity(conducteurDTO);
        Conducteur nouveauConducteur = conducteurService.creer(conducteur);
        return conducteurMapper.toDTO(nouveauConducteur);
    }

    @GetMapping//
    public List<ConducteurDTO> lister() {
        return conducteurService.lister()
                .stream()
                .map(conducteurMapper::toDTO)
                .toList();//immutable
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
            @Valid @RequestBody ConducteurDTO conducteurDTO) {

        Conducteur conducteur = conducteurMapper.toEntity(conducteurDTO);
        Conducteur modifie = conducteurService.modifier(id, conducteur);

        if (modifie == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Conducteur introuvable"
            );
        }

        return conducteurMapper.toDTO(modifie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        if (conducteurService.chercherParId(id) == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Conducteur introuvable"
            );
        }

        conducteurService.supprimer(id);
    }
}
