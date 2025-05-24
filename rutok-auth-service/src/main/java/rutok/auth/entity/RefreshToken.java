package rutok.auth.entity;

import java.time.*;
import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private Long userId;

    @Column
    private UUID accessJti;

    @Column
    private String token;

    @Column
    private UUID refreshJti;

    @Column
    private ZonedDateTime issuedAt;

    @Column
    private ZonedDateTime expiredAt;

}
