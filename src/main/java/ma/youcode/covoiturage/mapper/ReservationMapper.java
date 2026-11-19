
package ma.youcode.covoiturage.mapper;

import ma.youcode.covoiturage.dto.ReservationDTO;
import ma.youcode.covoiturage.entity.Reservation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "passagerId", source = "passager.id")
    @Mapping(target = "trajetId", source = "trajet.id")
    @Mapping(target = "pointMonteeId", source = "pointMontee.id")
    @Mapping(target = "pointDescenteId", source = "pointDescente.id")
    ReservationDTO toDTO(Reservation reservation);
}
