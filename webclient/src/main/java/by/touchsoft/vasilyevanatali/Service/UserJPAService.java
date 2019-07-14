package by.touchsoft.vasilyevanatali.Service;

import by.touchsoft.vasilyevanatali.Enum.UserRole;
import by.touchsoft.vasilyevanatali.Model.UserJPA;
import by.touchsoft.vasilyevanatali.Repository.IUserJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserJPAService {

    @Autowired
    IUserJPARepository userJPARepository;


    public List<UserJPA> findAllByRoles(UserRole role) {
        return userJPARepository.findAllByRoles(role);
    }

    public List<UserJPA> findAll() {
        return userJPARepository.findAll();
    }


    public UserJPA findByName(String name) {
        return userJPARepository.findByName(name);
    }

    public UserJPA save(UserJPA user) {
        return userJPARepository.save(user);
    }

    public UserJPA findById(Long id) {
        return userJPARepository.findById(id).get();
    }

    public void delete(UserJPA user) {
        userJPARepository.delete(user);
    }

//    public UserJPA findUserByNameAndRole(String name, UserRole role) {
//        return userJPARepository.findUserByNameAndRole(name, role);
//    }
}


