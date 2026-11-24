
package ma.youcode.covoiturage.repository;

import ma.youcode.covoiturage.entity.Reservation;
import ma.youcode.covoiturage.entity.StatutReservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;


// Repository pour gérer les réservations dans la base de données.
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    java.util.List<Reservation> findByPassager_IdOrderByDateReservationDesc(Long passagerId);

    java.util.List<Reservation> findByTrajet_Conducteur_IdOrderByDateReservationDesc(Long conducteurId);

    // Calcule le nombre total de places réservées pour un trajet et un statut donnés.
    @Query("""
        SELECT COALESCE(SUM(r.nombrePlaces), 0)
        FROM Reservation r
        WHERE r.trajet.id = :trajetId
        AND r.statut = :statut
    """)
    // Reçoit l'identifiant du trajet et le statut des réservations à prendre en compte.
    Long compterPlacesReservees(
            @Param("trajetId") Long trajetId,
            @Param("statut") StatutReservation statut
    );

    // Vérifie si un passager possède déjà une réservation
    // pour un trajet avec un statut donné.
    boolean existsByPassager_IdAndTrajet_IdAndStatut(
            Long passagerId,
            Long trajetId,
            StatutReservation statut
    );

    // Vérifie si une autre réservation existe pour le même passager et le même trajet,
    // en ignorant la réservation ayant l'identifiant indiqué.
    boolean existsByPassager_IdAndTrajet_IdAndStatutAndIdNot(
            Long passagerId,
            Long trajetId,
            StatutReservation statut,
            Long id
    );

}
