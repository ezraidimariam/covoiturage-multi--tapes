package ma.youcode.covoiturage.mapper;

import ma.youcode.covoiturage.dto.ConducteurDTO;
import ma.youcode.covoiturage.entity.Conducteur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConducteurMapper {

    ConducteurDTO toDTO(Conducteur conducteur);

    Conducteur toEntity(ConducteurDTO conducteurDTO);
}