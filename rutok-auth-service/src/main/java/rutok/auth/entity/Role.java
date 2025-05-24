package rutok.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.*;

import static lombok.AccessLevel.*;

@Getter
@Setter
@Builder
@Entity(name = "roles")
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Role implements GrantedAuthority {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String code;

    @Override
    public String getAuthority() {
        return code;
    }

}
