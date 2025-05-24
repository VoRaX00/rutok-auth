package rutok.auth.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import rutok.auth.entity.*;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {

    Optional<Credential> findByUserId(Long userId);

    Optional<Credential> findByUserIdAndHash(Long userId, String hash);

}
