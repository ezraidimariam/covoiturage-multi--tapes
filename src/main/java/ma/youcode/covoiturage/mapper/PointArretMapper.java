
package ma.youcode.covoiturage.mapper;

import ma.youcode.covoiturage.dto.PointArretDTO;
import ma.youcode.covoiturage.entity.PointArret;
import ma.youcode.covoiturage.entity.Trajet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PointArretMapper {

    @Mapping(target = "trajetId", source = "trajet.id")
    PointArretDTO toDTO(PointArret pointArret);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trajet", source = "trajet")
    PointArret toEntity(PointArretDTO dto, Trajet trajet);
}
