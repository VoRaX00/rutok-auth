package rutok.auth;

import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.*;
import jakarta.servlet.http.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.security.web.*;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException ex
    ) throws IOException {
        log.error("Exception while authorizing", ex);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        var os = response.getOutputStream();
        var dto = Map.of("message", ex.getMessage());
        mapper.writeValue(os, dto);
        os.flush();
    }

}
