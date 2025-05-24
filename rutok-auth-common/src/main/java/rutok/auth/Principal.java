package rutok.auth;

import java.util.*;

import lombok.*;

import static lombok.AccessLevel.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Principal {

    private long id;

    private String login;

    private List<String> roles;

}
