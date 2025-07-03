package rutok.auth.dto;

import java.time.*;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private Boolean banned;

    private String username;

    private String email;

    private String roleCode;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;
}
