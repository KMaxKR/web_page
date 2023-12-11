package ks.msx.web_page.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {
    private String username;
    private String password;
}
