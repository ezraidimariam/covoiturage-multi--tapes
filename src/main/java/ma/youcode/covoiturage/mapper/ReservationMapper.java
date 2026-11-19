package ma.youcode.covoiturage.mapper;

import ma.youcode.covoiturage.dto.ReservationDTO;
import ma.youcode.covoiturage.entity.Reservation;
import ma.youcode.covoiturage.entity.Passager;
import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.entity.PointArret;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "passagerId", source = "passager.id")
    @Mapping(target = "trajetId", source = "trajet.id")
    @Mapping(target = "pointMonteeId", source = "pointMontee.id")
    @Mapping(target = "pointDescenteId", source = "pointDescente.id")
    ReservationDTO toDTO(Reservation reservation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statut", source = "dto.statut")
    @Mapping(target = "passager", source = "passager")
    @Mapping(target = "trajet", source = "trajet")
    @Mapping(target = "pointMontee", source = "pointMontee")
    @Mapping(target = "pointDescente", source = "pointDescente")
    Reservation toEntity(
            ReservationDTO dto,
            Passager passager,
            Trajet trajet,
            PointArret pointMontee,
            PointArret pointDescente
    );
}