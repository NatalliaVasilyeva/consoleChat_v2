package by.touchsoft.vasilyevanatali.Repository;

import by.touchsoft.vasilyevanatali.Enum.UserRole;
import by.touchsoft.vasilyevanatali.Model.UserJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserJPARepository extends JpaRepository<UserJPA, Long> {
   List<UserJPA> findAllByRoles(UserRole role);
   List<UserJPA> findAll();
   UserJPA findByName(String name);
   Optional<UserJPA> findById(Long id);
//   UserJPA findUserByNameAndRole(String name, UserRole role);
}
