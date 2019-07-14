package by.touchsoft.vasilyevanatali.Service;

import by.touchsoft.vasilyevanatali.Enum.UserRole;
import by.touchsoft.vasilyevanatali.Model.UserJPA;
import by.touchsoft.vasilyevanatali.Repository.UserJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserJPAService {

    @Autowired
    UserJPARepository userJPARepository;


    public List<UserJPA> findAllByRole(UserRole role) {
        return userJPARepository.findAllByRole(role);
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

    public UserJPA findById(Integer id) {
        return userJPARepository.findById(id).get();
    }

    public void delete(UserJPA user) {
        userJPARepository.delete(user);
    }

    public UserJPA findUserByNameAndRole(String name, UserRole role) {
        return userJPARepository.findUserByNameAndRole(name, role);
    }

    public UserJPA saveUserInDB(String name, UserRole role) {
        if(userJPARepository.findUserByNameAndRole(name, role)!=null){
            return null;
        }
        UserJPA userJPA = new UserJPA(name, role);
        userJPA.setRegistrationTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println(userJPA);
        return userJPARepository.saveAndFlush(userJPA);
    }


}


