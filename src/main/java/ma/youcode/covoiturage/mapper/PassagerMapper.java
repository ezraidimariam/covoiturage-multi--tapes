
package ma.youcode.covoiturage.mapper;

import ma.youcode.covoiturage.dto.PassagerDTO;
import ma.youcode.covoiturage.entity.Passager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PassagerMapper {

    PassagerDTO toDTO(Passager passager);

    @Mapping(target = "id", ignore = true)
    Passager toEntity(PassagerDTO passagerDTO);
}
