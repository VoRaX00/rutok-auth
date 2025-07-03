package rutok.auth.client;

import org.springframework.cloud.openfeign.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import rutok.auth.dto.*;

@FeignClient(
    name = "userClient",
    url = "${users.client.url}"
)
public interface UserClient {

    @PostMapping("/api/users/register")
    ResponseEntity<CreateUserResponse> createUser(@RequestBody RegisterDto user);

    @PostMapping("/api/users/verify")
    ResponseEntity<UserDto> check(@RequestBody LoginRequest loginRequest);

    @GetMapping("/api/users/{id}")
    ResponseEntity<UserDto> getUser(@PathVariable Long id);

}
