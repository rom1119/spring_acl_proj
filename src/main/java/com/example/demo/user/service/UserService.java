package com.example.demo.user.service;

import com.example.demo.acl.service.CustomAclService;
import com.example.demo.user.exception.EmailExistsException;
import com.example.demo.user.model.CustomUserDetails;
import com.example.demo.user.model.User;
import com.example.demo.user.model.UserDetails;
import com.example.demo.user.model.UserDto;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserDetailsRepository;
import com.example.demo.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    private CustomAclService aclService;

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private EntityManager entityManager;

//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }


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
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
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
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));

        userRepository.save(user);

        return user;
    }

    @Override
    public User updateUser(User userDb, UserDto userDto) throws Exception {

        User user = modelMapper.map(userDto, User.class);
        user.getUserDetails().setFileName(userDb.getUserDetails().getFileName());
        user.setRoles(userDb.getRoles());

        userRepository.save(user);

        return user;
    }

    @Override
    public User changeRoles(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).get();
        user.setRoles(userDto.getRoles());

        userRepository.save(user);

        return user;
    }

    @Override
    public Page<User> findAll(CustomUserDetails userdetails, Pageable pageable) {
        if (userdetails.hasSuperAdmin()) {
            return userRepository.findAll(pageable);
        }
        Permission read = BasePermission.READ;
        return userRepository.findAllCustomPageable(User.class.getName(), userdetails.getUser().getEmail(), read.getMask(), pageable);
    }

    @Override
    public Page<User> findBySearchTerm(CustomUserDetails user, String term, Pageable pageable) {
        if (user.hasSuperAdmin()) {
            return userRepository.findAll(term, pageable);
        }
        Permission read = BasePermission.READ;
        return userRepository.findBySearchTermPageable(User.class.getName(), user.getUser().getEmail(), read.getMask(), term, pageable);

    }

    @Override
    public User findByIdToView(Long id) throws NoSuchElementException {
        return findOne(id);
    }

    @Override
    public User findByIdToEdit(Long id) throws NoSuchElementException {
        return findOne(id);
    }

    @Override
    public User findByIdToDelete(Long id) throws NoSuchElementException {
        return findOne(id);
    }

    @Override
    public User findByIdToAdministration(Long id) throws NoSuchElementException {
        return findOne(id);
    }

    @Override
    public User findByIdToChangePassword(Long id) throws NoSuchElementException {
        return findOne(id);
    }

    @Override
    public User findByIdToChangeRoles(Long id) throws NoSuchElementException {
        return findOne(id);
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

    private List<User> getOneMethod(User user) throws NoSuchElementException
    {
        User toEdit = findOne(user.getId());
        List<User> arr = new ArrayList<>();
        if (toEdit != null) {
            arr.add(toEdit);
        }
        return arr;
    }

    @Override
    public List<User> getOneToChangePassword(User user) {
        return getOneMethod(user);
    }

    @Override
    public List<User> getOneToEdit(User user) {
        return getOneMethod(user);
    }

    @Override
    public List<User> getOneToDelete(User user) {
        return getOneMethod(user);
    }

    @Override
    public List<User> getOneToAdministration(User user) {
        return getOneMethod(user);
    }

    @Override
    public List<User> getOneToChangeRoles(User user) {
        return getOneMethod(user);
    }

    private boolean emailExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    private User findOne(Long id) throws NoSuchElementException {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

}
