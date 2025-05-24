package rutok.auth.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginModel {

    private String email;

    private String password;

}
