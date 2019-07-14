package by.touchsoft.vasilyevanatali.Model;

import by.touchsoft.vasilyevanatali.Enum.UserRole;
import by.touchsoft.vasilyevanatali.Enum.UserType;
import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "users")
public class UserJPA  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer userId;


    @Column(name = "name")
    private String name;


   @Column(name="role")
    private UserRole role;


   @Column(name="type")
    private UserType type;

    @Column(name = "password")
    private String password;


    @Column(name = "reg_time")
    private String registrationTime;

    public UserJPA(String name, UserRole role) {
        this.name = name;
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJPA userJPA = (UserJPA) o;
        return Objects.equals(userId, userJPA.userId) &&
                Objects.equals(name, userJPA.name) &&
                Objects.equals(role, userJPA.role) &&
                Objects.equals(type, userJPA.type) &&
                Objects.equals(password, userJPA.password) &&
                Objects.equals(registrationTime, userJPA.registrationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, role, type, password, registrationTime);
    }

    @Override
    public String toString() {
        String sb = "UserJPA{" + "userId=" + userId +
                ", name='" + name + '\'' +
                ", roles=" + role +
                ", type=" + type +
                ", password='" + password + '\'' +
                ", registrationTime='" + registrationTime + '\'' +
                '}';
        return sb;
    }
}
