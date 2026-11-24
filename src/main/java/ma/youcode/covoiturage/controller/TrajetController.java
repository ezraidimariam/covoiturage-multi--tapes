
package ma.youcode.covoiturage.controller;

import jakarta.validation.Valid;
import ma.youcode.covoiturage.dto.TrajetDTO;
import ma.youcode.covoiturage.dto.TrajetDetailDTO;
import ma.youcode.covoiturage.dto.RechercheTrajetDTO;
import ma.youcode.covoiturage.dto.TrajetRechercheResultatDTO;
import ma.youcode.covoiturage.entity.Conducteur;
import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.entity.Vehicule;
import ma.youcode.covoiturage.mapper.TrajetMapper;
import ma.youcode.covoiturage.service.ConducteurService;
import ma.youcode.covoiturage.service.TrajetService;
import ma.youcode.covoiturage.service.VehiculeService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ma.youcode.covoiturage.mapper.PointArretMapper pointArretMapper;

    public TrajetController(
            TrajetService trajetService,
            ConducteurService conducteurService,
            VehiculeService vehiculeService,
            TrajetMapper trajetMapper,
            ma.youcode.covoiturage.mapper.PointArretMapper pointArretMapper) {

        this.trajetService = trajetService;
        this.conducteurService = conducteurService;
        this.vehiculeService = vehiculeService;
        this.trajetMapper = trajetMapper;
        this.pointArretMapper = pointArretMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrajetDTO creer(@Valid @RequestBody TrajetDTO trajetDTO) {

        Conducteur conducteur =
                conducteurService.chercherParId(trajetDTO.conducteurId());

        if (conducteur == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Conducteur introuvable"
            );
        }

        Vehicule vehicule =
                vehiculeService.chercherParId(trajetDTO.vehiculeId());

        if (vehicule == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Vehicule introuvable"
            );
        }

        if (!vehicule.getConducteur().getId().equals(conducteur.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Ce véhicule n'appartient pas à ce conducteur"
            );
        }

        if (trajetDTO.placesProposees() > vehicule.getCapacite()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le nombre de places dépasse la capacité du véhicule"
            );
        }

        Trajet trajet = trajetMapper.toEntity(
                trajetDTO,
                conducteur,
                vehicule
        );

        return trajetMapper.toDTO(trajetService.creer(trajet));
    }

    @GetMapping
    public List<TrajetDTO> lister() {

        return trajetService.lister()
                .stream()
                .map(trajetMapper::toDTO)
                .toList();
    }

    // Recherche avec pagination
    @GetMapping("/rechercher")
    public Page<TrajetRechercheResultatDTO> rechercher(
            @Valid @ModelAttribute RechercheTrajetDTO criteres,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 0 || size < 1 || size > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La page doit être positive et la taille comprise entre 1 et 100"
            );
        }

        // Création de la pagination sans tri.
        // Le tri est déjà défini dans la requête du Repository.
        Pageable pagination = PageRequest.of(page, size);

        return trajetService.rechercher(criteres, pagination);
    }

    @GetMapping("/{id}")
    public TrajetDetailDTO chercherParId(@PathVariable Long id) {

        Trajet trajet = trajetService.chercherParId(id);
        return new TrajetDetailDTO(
                trajet.getId(),
                trajet.getVilleDepart(),
                trajet.getVilleArrivee(),
                trajet.getDateDepart(),
                trajet.getPlacesProposees(),
                trajetService.placesRestantes(id),
                trajet.getStatut(),
                trajet.getConducteur().getId(),
                trajet.getConducteur().getPrenom() + " " + trajet.getConducteur().getNom(),
                trajet.getVehicule().getId(),
                trajet.getPointsArret().stream()
                        .sorted(java.util.Comparator.comparingInt(
                                ma.youcode.covoiturage.entity.PointArret::getOrdre))
                        .map(pointArretMapper::toDTO)
                        .toList()
        );
    }

    @PutMapping("/{id}")
    public TrajetDTO modifier(
            @PathVariable Long id,
            @Valid @RequestBody TrajetDTO trajetDTO) {

        Conducteur conducteur =
                conducteurService.chercherParId(trajetDTO.conducteurId());

        if (conducteur == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Conducteur introuvable"
            );
        }

        Vehicule vehicule =
                vehiculeService.chercherParId(trajetDTO.vehiculeId());

        if (vehicule == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Vehicule introuvable"
            );
        }

        if (!vehicule.getConducteur().getId().equals(conducteur.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Ce véhicule n'appartient pas à ce conducteur"
            );
        }

        if (trajetDTO.placesProposees() > vehicule.getCapacite()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le nombre de places dépasse la capacité du véhicule"
            );
        }

        Trajet trajet = trajetMapper.toEntity(
                trajetDTO,
                conducteur,
                vehicule
        );

        Trajet modifie = trajetService.modifier(id, trajet);

        if (modifie == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Trajet introuvable"
            );
        }

        return trajetMapper.toDTO(modifie);
    }

    @PostMapping("/{id}/publier")
    public TrajetDTO publier(@PathVariable Long id) {

        return trajetMapper.toDTO(trajetService.publier(id));
    }

    @PostMapping("/{id}/annuler")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void annuler(@PathVariable Long id) {
        trajetService.annuler(id);
    }

    @PostMapping("/{id}/terminer")
    public TrajetDTO terminer(@PathVariable Long id) {
        return trajetMapper.toDTO(trajetService.terminer(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {

        trajetService.supprimer(id);
    }
}
