package rutok.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponse {

    private Long id;

    private String roleCode;

}
