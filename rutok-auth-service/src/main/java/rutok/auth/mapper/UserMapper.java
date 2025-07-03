package rutok.auth.mapper;

import org.mapstruct.*;
import rutok.auth.dto.*;
import rutok.auth.entity.*;
import rutok.auth.model.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserModel toModel(RegisterDto registerDto);

    UserModel toModel(UserDto userDto);

    UserModel toModel(CreateUserDto createUserDto);

    CreateUserDto toDto(RegisterDto registerDto);

}
