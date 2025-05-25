package rutok.auth.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import rutok.auth.entity.*;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByRefreshJti(UUID jti);

    void deleteByRefreshJti(UUID jti);

    void deleteByUserId(Long userId);

}
