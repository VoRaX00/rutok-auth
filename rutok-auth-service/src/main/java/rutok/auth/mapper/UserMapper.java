package rutok.auth.mapper;

import org.mapstruct.*;
import rutok.auth.dto.*;
import rutok.auth.entity.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "gender", source = "gender", qualifiedByName = "mappingGender")
    User toEntity(RegisterDto dto);

    @Named("mappingGender")
    default Gender mappingGender(String gender) {
        return Gender.valueOf(gender);
    }

}
