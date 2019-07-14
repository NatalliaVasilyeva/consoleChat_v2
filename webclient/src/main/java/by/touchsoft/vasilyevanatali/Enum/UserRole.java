package by.touchsoft.vasilyevanatali.Enum;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole  implements GrantedAuthority {
    AGENT, CLIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}

