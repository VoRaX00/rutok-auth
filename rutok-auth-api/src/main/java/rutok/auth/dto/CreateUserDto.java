package rutok.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    private String username;

    private String email;

    private String hashedPassword;

}
