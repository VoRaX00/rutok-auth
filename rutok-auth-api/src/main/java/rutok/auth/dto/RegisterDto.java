package rutok.auth.dto;

import java.time.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    private String email;

    private String name;

    private String gender;

    private LocalDate birthday;

    private String password;

}
