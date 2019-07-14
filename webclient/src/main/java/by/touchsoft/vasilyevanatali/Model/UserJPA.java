package by.touchsoft.vasilyevanatali.Model;

import by.touchsoft.vasilyevanatali.Enum.UserRole;
import by.touchsoft.vasilyevanatali.Enum.UserType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "users")
public class UserJPA {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long userId;


    @Column(name = "name")
    private String name;


    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;


    @ElementCollection(targetClass = UserType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_types", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserType> type;

    @Column(name = "password")
    private String password;


    @Column(name = "reg_time")
    private String registrationTime;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public Set<UserType> getType() {
        return type;
    }

    public void setType(Set<UserType> type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public void addRole(UserRole role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJPA userJPA = (UserJPA) o;
        return Objects.equals(userId, userJPA.userId) &&
                Objects.equals(name, userJPA.name) &&
                Objects.equals(roles, userJPA.roles) &&
                Objects.equals(type, userJPA.type) &&
                Objects.equals(password, userJPA.password) &&
                Objects.equals(registrationTime, userJPA.registrationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, roles, type, password, registrationTime);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserJPA{");
        sb.append("userId=").append(userId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", roles=").append(roles);
        sb.append(", type=").append(type);
        sb.append(", password='").append(password).append('\'');
        sb.append(", registrationTime='").append(registrationTime).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
