package rutok.auth.service;

import lombok.*;
import org.springframework.stereotype.*;
import rutok.auth.entity.*;
import rutok.auth.repository.*;

@Service
@RequiredArgsConstructor
public class CredentialsService {

    private final CredentialRepository credentialsRepository;

    public void save(Credential credential) {
        credentialsRepository.save(credential);
    }

}
