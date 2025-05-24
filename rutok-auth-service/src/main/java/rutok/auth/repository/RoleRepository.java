package rutok.auth.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import rutok.auth.entity.*;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
