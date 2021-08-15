package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor(staticName = "withLoginRequestBody")
@Accessors(chain = true)
public class LoginRequestBody {

    private String email;
    private String password;
}
