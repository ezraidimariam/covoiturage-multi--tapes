
package ma.youcode.covoiturage.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import ma.youcode.covoiturage.entity.StatutReservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationDTO(
        Long id,

        @Min(1)
        int nombrePlaces,

        BigDecimal montantTotal,

        LocalDateTime dateReservation,

        StatutReservation statut,

        @NotNull
        Long passagerId,

        @NotNull
        Long trajetId,

        @NotNull
        Long pointMonteeId,

        @NotNull
        Long pointDescenteId
) {}
