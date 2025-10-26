package model;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {

    @Expose
    private String email;

    @Expose(deserialize = false)
    private String senha;
}