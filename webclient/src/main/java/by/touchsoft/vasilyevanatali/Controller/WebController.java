//package by.touchsoft.vasilyevanatali.Controller;
//
//import by.touchsoft.vasilyevanatali.Model.User;
//import by.touchsoft.vasilyevanatali.Repository.UserRepository;
//import by.touchsoft.vasilyevanatali.Service.UserServiceSingleton;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @author Natali
// * Login or singUp to program
// */
//
//@Controller
//public class WebController {
//
//    @GetMapping("/login")
//    public String getLoginPage(Authentication authentication, ModelMap model, HttpServletRequest request) {
//
//        if (authentication != null) {
//            return "redirect:/";
//        }
//
//        if (request.getParameterMap().containsKey("error")) {
//            model.addAttribute("error", true);
//        }
//        return "login";
//    }
//
//
//    @GetMapping("/signUp")
//    public String getSignUpPage() {
//        return "signUp";
//
//    }
//
//
//    @PostMapping("/signUp")
//    public String signUp(User user) {
//
//        user.setRestClient(true);
//        UserServiceSingleton.INSTANCE.addUser(user);
//        UserRepository.INSTANCE.addUser(user);
//
//        return "redirect:/login";
//
//    }
//}
