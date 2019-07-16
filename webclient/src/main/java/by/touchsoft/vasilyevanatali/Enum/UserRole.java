package by.touchsoft.vasilyevanatali.Enum;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Natali
 *
 * Class contain user role
 */

public enum UserRole  implements GrantedAuthority {
    AGENT, CLIENT;

    /**
     *
     * @return name of role to authentication
     */
    @Override
    public String getAuthority() {
        return name();
    }
}

