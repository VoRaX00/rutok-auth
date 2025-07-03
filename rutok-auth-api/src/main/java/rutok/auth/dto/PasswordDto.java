package rutok.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDto {

    private String email;

    private String password;

}
