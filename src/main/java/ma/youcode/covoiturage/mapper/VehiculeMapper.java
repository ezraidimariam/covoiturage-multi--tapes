package ma.youcode.covoiturage.mapper;

import ma.youcode.covoiturage.dto.VehiculeDTO;
import ma.youcode.covoiturage.entity.Conducteur;
import ma.youcode.covoiturage.entity.Vehicule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {

    @Mapping(target = "conducteurId", source = "conducteur.id")
    VehiculeDTO toDTO(Vehicule vehicule);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "conducteur", source = "conducteur")
    Vehicule toEntity(VehiculeDTO vehiculeDTO, Conducteur conducteur);
}