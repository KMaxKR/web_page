package ks.msx.web_page.entity.user_permisions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Permissions {

    READ("READ"),
    WRITE("WRITE"),
    DELETE("DELETE"),
    UPDATE("UPDATE");

    @Getter
    private final String getPermissions;
}
