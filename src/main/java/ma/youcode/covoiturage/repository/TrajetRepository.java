
package ma.youcode.covoiturage.repository;

import ma.youcode.covoiturage.dto.TrajetRechercheResultatDTO;
import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.entity.StatutTrajet;
import ma.youcode.covoiturage.entity.StatutReservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {

    boolean existsByVehicule_Id(Long vehiculeId);

    @Query(
            value = """
            SELECT new ma.youcode.covoiturage.dto.TrajetRechercheResultatDTO(
                t,
                pDescente.prixDepuisDepart - pMontee.prixDepuisDepart,
                t.placesProposees - COALESCE(
                    (SELECT SUM(r.nombrePlaces)
                     FROM Reservation r
                     WHERE r.trajet.id = t.id
                     AND r.statut = :statutReservation),
                    0L
                )
            )
            FROM Trajet t
            JOIN t.pointsArret pMontee
            JOIN t.pointsArret pDescente
            WHERE t.statut = :statutTrajet
            AND t.dateDepart >= :debutJournee
            AND t.dateDepart < :lendemain
            AND LOWER(pMontee.ville) = LOWER(:villeDepart)
            AND LOWER(pDescente.ville) = LOWER(:villeArrivee)
            AND pMontee.ordre < pDescente.ordre
            AND t.placesProposees - COALESCE(
                (SELECT SUM(r.nombrePlaces)
                 FROM Reservation r
                 WHERE r.trajet.id = t.id
                 AND r.statut = :statutReservation),
                0L
            ) >= :nombrePlaces
            AND (
                :prixMax IS NULL
                OR pDescente.prixDepuisDepart - pMontee.prixDepuisDepart <= :prixMax
            )
            ORDER BY t.dateDepart ASC
            """,
            countQuery = """
            SELECT COUNT(DISTINCT t.id)
            FROM Trajet t
            JOIN t.pointsArret pMontee
            JOIN t.pointsArret pDescente
            WHERE t.statut = :statutTrajet
            AND t.dateDepart >= :debutJournee
            AND t.dateDepart < :lendemain
            AND LOWER(pMontee.ville) = LOWER(:villeDepart)
            AND LOWER(pDescente.ville) = LOWER(:villeArrivee)
            AND pMontee.ordre < pDescente.ordre
            AND t.placesProposees - COALESCE(
                (SELECT SUM(r.nombrePlaces)
                 FROM Reservation r
                 WHERE r.trajet.id = t.id
                 AND r.statut = :statutReservation),
                0L
            ) >= :nombrePlaces
            AND (
                :prixMax IS NULL
                OR pDescente.prixDepuisDepart - pMontee.prixDepuisDepart <= :prixMax
            )
            """
    )
    Page<TrajetRechercheResultatDTO> rechercher(
            @Param("villeDepart") String villeDepart,
            @Param("villeArrivee") String villeArrivee,
            @Param("debutJournee") LocalDateTime debutJournee,
            @Param("lendemain") LocalDateTime lendemain,
            @Param("nombrePlaces") int nombrePlaces,
            @Param("prixMax") BigDecimal prixMax,
            @Param("statutTrajet") StatutTrajet statutTrajet,
            @Param("statutReservation") StatutReservation statutReservation,
            Pageable pageable
    );
}
