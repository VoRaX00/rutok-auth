package rutok.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {

    private String type;

    private String message;

}
