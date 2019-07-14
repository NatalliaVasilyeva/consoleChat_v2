package by.touchsoft.vasilyevanatali.Repository;

import by.touchsoft.vasilyevanatali.Enum.UserRole;
import by.touchsoft.vasilyevanatali.Model.UserJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJPARepository extends JpaRepository<UserJPA, Integer> {
   List<UserJPA> findAllByRole(UserRole role);
   List<UserJPA> findAll();
   UserJPA findByName(String name);
   Optional<UserJPA> findById(Integer id);
   UserJPA findUserByNameAndRole(String name, UserRole role);
   UserJPA saveAndFlush(UserJPA userJPA);
}
