package ks.msx.web_page.entity.user_permisions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public enum Role {
    USER(Set.of(
            Permissions.READ
    )),
    ADMIN(Set.of(
            Permissions.READ,
            Permissions.WRITE,
            Permissions.UPDATE
    )),
    ROOT(Set.of(
            Permissions.READ,
            Permissions.WRITE,
            Permissions.UPDATE,
            Permissions.DELETE
    ));

    @Getter
    private final Set<Permissions> getPermissions;

    public List<SimpleGrantedAuthority> getAuthority(){
        List<SimpleGrantedAuthority> list = new ArrayList<>(getPermissions.stream()
                .map(permissions -> new SimpleGrantedAuthority(permissions.name()))
                .toList());
        list.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return list;
    }
}
