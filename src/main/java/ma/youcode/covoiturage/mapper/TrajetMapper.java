
package ma.youcode.covoiturage.mapper;

import ma.youcode.covoiturage.dto.TrajetDTO;
import ma.youcode.covoiturage.entity.Conducteur;
import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.entity.Vehicule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrajetMapper {

    @Mapping(target = "conducteurId", source = "conducteur.id")
    @Mapping(target = "vehiculeId", source = "vehicule.id")
    TrajetDTO toDTO(Trajet trajet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "conducteur", source = "conducteur")
    @Mapping(target = "vehicule", source = "vehicule")
    @Mapping(target = "pointsArret", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    Trajet toEntity(TrajetDTO trajetDTO,
                    Conducteur conducteur,
                    Vehicule vehicule);
}
