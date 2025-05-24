package rutok.auth.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import rutok.auth.entity.*;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

}
