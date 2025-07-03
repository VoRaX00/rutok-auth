package rutok.auth.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    private Long id;

    private String roleCode;

    private String username;

    private String email;

}
