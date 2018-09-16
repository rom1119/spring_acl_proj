package com.example.demo.user.service;

import com.example.demo.acl.config.CustomPermission;
import com.example.demo.acl.config.CustomUserDetails;
import com.example.demo.acl.service.CustomAclService;
import com.example.demo.user.exception.EmailExistsException;
import com.example.demo.user.model.User;
import com.example.demo.user.model.UserDetails;
import com.example.demo.user.model.UserDto;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserDetailsRepository;
import com.example.demo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EntityManagerFactory emf;

    private Authentication authentication;

    @Autowired
    private CustomAclService aclService;

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
        user.setPassword(passwordEncoder().encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.setEnabled(true);
        user.addRole(roleRepository.findByName("ROLE_USER"));

        UserDetails userDetailsAdmin = new UserDetails();
        userDetailsAdmin.setScore(0);
        userDetailsAdmin.setFirstName(accountDto.getUserDetails().getFirstName());
        userDetailsAdmin.setLastName(accountDto.getUserDetails().getLastName());;

        userDetailsAdmin.setUser(user);

        userDetailsRepository.save(userDetailsAdmin);
        userRepository.save(user);

        aclService.createAclWithUserSid(user.getClass(), user.getId(), user);

        return user;
    }

    @Override
    public User changePassword(UserDto accountDto) {

        User user = userRepository.findById(accountDto.getId()).get();
        user.setPassword(passwordEncoder().encode(accountDto.getPassword()));

        userRepository.save(user);

        return user;
    }

    @Override
    public User updateUser(UserDto accountDto) throws Exception {
        return null;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByIdToView(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findByIdToEdit(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findByIdToDelete(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findByIdToAdministration(Long id) {
        return userRepository.findById(id).get();
    }
    @Override
    public User findByIdToChangePassword(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public boolean canDelete(User user) {
        return getOneToDelete(user).size() > 0;
    }

    @Override
    public boolean canEdit(User user) {
        System.out.println(getOneToEdit(user).size());
        return getOneToEdit(user).size() > 0;
    }

    @Override
    public boolean canChangePassword(User user) {
        return getOneToChangePassword(user).size() > 0;
    }


    @Override
    public List<User> getOneToChangePassword(User user) {
        User toEdit = findByIdToChangePassword(user.getId());
        return Arrays.asList(toEdit);
    }

    @Override
    public List<User> getOneToEdit(User user) {
        User toEdit = findOne(user.getId());
        List<User> arr = new ArrayList<>();
        if (toEdit != null) {
            arr.add(toEdit);
        }
        return arr;
    }

    @Override
    public List<User> getOneToDelete(User user) {
        User toEdit = findOne(user.getId());
        return Arrays.asList(toEdit);
    }




    @Override
    public List<User> getOneToAdministration(User user) {
        User toEdit = findOne(user.getId());
        return Arrays.asList(toEdit);
    }

    private boolean emailExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    private User findOne(Long id) {
        return userRepository.findById(id).get();
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}
