package com.example.demo.main.service;

import com.example.demo.user.exception.EmailExistsException;
import com.example.demo.user.model.User;
import com.example.demo.user.model.UserDetails;
import com.example.demo.user.model.UserDto;
import com.example.demo.main.repository.RoleRepository;
import com.example.demo.main.repository.UserDetailsRepository;
import com.example.demo.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EntityManagerFactory emf;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    private EntityManager entityManager;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Transactional
    @Override
    public User registerNewUserAccount(UserDto accountDto)
            throws EmailExistsException {

        if (emailExist(accountDto.getEmail())) {
            throw new EmailExistsException(
                    "There is an account with that email adress: "
                            +  accountDto.getEmail());
        }


//        entityManager = emf.createEntityManager();

        User user = new User();
        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setPassword(passwordEncoder().encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.addRole(roleRepository.findByName("ROLE_USER"));

        UserDetails userDetailsAdmin = new UserDetails();
        userDetailsAdmin.setScore(0);

        userDetailsAdmin.setUser(user);

        userDetailsRepository.save(userDetailsAdmin);
        userRepository.save(user);

        return user;
    }

    @Override
    public User updateUser(UserDto accountDto) throws Exception {
        return null;
    }


    private boolean emailExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }
}
