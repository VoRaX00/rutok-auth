package rutok.auth.mapper;

import org.mapstruct.*;
import rutok.auth.dto.*;
import rutok.auth.model.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthMapper {

    LoginModel toModel(LoginRequest loginRequest);

    AuthDto toDto(AuthModel authModel);

}
