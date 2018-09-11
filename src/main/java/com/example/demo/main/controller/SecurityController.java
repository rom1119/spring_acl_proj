package com.example.demo.main.controller;

import com.example.demo.user.exception.EmailExistsException;
import com.example.demo.user.model.User;
import com.example.demo.user.model.UserDto;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManagerFactory;
import javax.validation.Valid;
import java.util.Collection;

@Controller
public class SecurityController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private IUserService service;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerForm(WebRequest request) {
//        System.out.println("/user/register");


        UserDto userDto = new UserDto();
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("user", userDto);
        modelAndView.addObject("userr", "ghgw");

        return modelAndView;
    }

    @RequestMapping(path="/register", method = RequestMethod.POST, produces={"application/json"})
    public ModelAndView registerProcess(
            @ModelAttribute("user") @Valid UserDto accountDto,
            BindingResult result,
            WebRequest request,
            Errors errors) {

//        System.out.println("/register");
//        System.out.println(result.hasErrors());

        result.getAllErrors().stream().forEach(e -> {
//            System.out.println(e.toString());

        });

        User registered = new User();
        if (!result.hasErrors()) {
            registered = createUserAccount(accountDto, result);
        }
        if (registered == null) {
            result.rejectValue("email", "message.regError");
        }

        if (result.hasErrors()) {
            return new ModelAndView("register", "user", accountDto);
        }
        else {
            return new ModelAndView("successRegister", "user", accountDto);
        }


//        Set<ConstraintViolation<User>> constraintViolations =
//                validator.validate( user );
//
//        if (constraintViolations.size() > 0) {
//            for(ConstraintViolation<User> error : constraintViolations){
//                System.out.println(error.getPropertyPath().toString() + ": " + error.getMessage());
//            }
//
////            return new ResponseEntity(user, HttpStatus.OK);
//
//        }
////            return new ResponseEntity(user, HttpStatus.OK);
//
//        entityManager.flush();

//        return ResponseEntity.ok(user);

    }


    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login(Authentication user){

        if (user != null && user.isAuthenticated()) {
            return "redirect:/login_success";
        }
        return "login";
    }

    @RequestMapping(path = "/login_success", method = RequestMethod.GET)
    public String loginSuccess(){

        return "loginSuccess";
    }

    @RequestMapping(path = "/logout_success", method = RequestMethod.GET)
    public String logoutSuccess(Authentication principal){
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        System.out.println(authorities);

        return "logoutSuccess";
    }

    //
////    @PreAuthorize("#oauth2.hasScope('read')")
//    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
//    public ResponseEntity<User> getUser(@PathVariable final String id)
//    {
//        System.out.println(id);
//        User user = userRepository.findById(id);
//
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(user);
//
//    }


    private User createUserAccount(UserDto accountDto, BindingResult result) {
        User registered = null;
        try {
            registered = service.registerNewUserAccount(accountDto);
        } catch (EmailExistsException e) {
            return null;
        }
        return registered;
    }
}
