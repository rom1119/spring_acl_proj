package com.example.demo;

import com.example.demo.acl.model.AclClass;
import com.example.demo.acl.model.AclObjectIdentity;
import com.example.demo.acl.model.AclSecurityID;
import com.example.demo.acl.repository.AclClassRepository;
import com.example.demo.acl.repository.AclObjectIdentityRepository;
import com.example.demo.acl.repository.AclSecurityIDRepository;
import com.example.demo.user.model.*;
import com.example.demo.user.repository.PrivilegeRepository;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public  class MyMockUserTestExecutionListener extends AbstractTestExecutionListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private AclSecurityIDRepository aclSecurityIDRepository;

    @Autowired
    private AclClassRepository aclClassRepository;

    @Autowired
    private AclObjectIdentityRepository aclObjectIdentityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    Role userRole = new Role("ROLE_USER");
    Role adminRole = new Role("ROLE_ADMIN");
    Role superAdminRole = new Role("ROLE_SUPER_ADMIN");

    Set<Role> userRoles = new HashSet<>();
    Set<Role> adminRoles = new HashSet<>();
    Set<Role> superAdminRoles = new HashSet<>();

    Privilege read = new Privilege("READ_PRIVILEGE");
    Privilege write = new Privilege("WRITE_PRIVILEGE");

    User user = new User("user", "test", true, new UserDetails("UserFirstName", "UserLastName"), userRoles);
    User userOff = new User("userDisable", "test", false, new UserDetails("UserDisableFirstName", "UserDisableLastName"), userRoles);
    User admin = new User("admin", "test", true, new UserDetails("AdminDisableFirstName", "AdminDisableLastName"), adminRoles);
    User adminOff = new User("adminDisable", "test", false, new UserDetails("UserDisableFirstName", "UserDisableLastName"), adminRoles);
    User superAdmin = new User("superAdmin", "test", true, new UserDetails("SuperAdminDisableFirstName", "SuperAdminDisableLastName"), superAdminRoles);
    User superAdminOff = new User("superAdminDisable", "test", false, new UserDetails("SuperAdminDisableFirstName", "SuperAdminDisableLastName"), superAdminRoles);

    org.springframework.security.core.userdetails.UserDetails loggedUser;
    org.springframework.security.core.userdetails.UserDetails loggedUserOff;
    org.springframework.security.core.userdetails.UserDetails loggedAdmin;
    org.springframework.security.core.userdetails.UserDetails loggedAdminOff;
    org.springframework.security.core.userdetails.UserDetails loggedSuperAdmin;
    org.springframework.security.core.userdetails.UserDetails loggedSuperAdminOff;

    Map<String, User> users = new HashMap<>();
    private MockMvc mockMvc;

    {
        users.put("user", user);
        users.put("userOff", userOff);
        users.put("admin", admin);
        users.put("adminOff", adminOff);
        users.put("superAdmin", superAdmin);
        users.put("superAdminOff", superAdminOff);

    }

    public void setup() throws Exception {
        System.out.println("BEFORE !!!!!!!!!!!!");
        read = createPrivilegeIfNotFound(read);
        write = createPrivilegeIfNotFound(write);

        Set<Privilege> superAdminPrivileges = new HashSet<Privilege>();
        Set<Privilege> adminPrivileges = new HashSet<Privilege>();
        Set<Privilege> userPrivileges = new HashSet<Privilege>();

        superAdminPrivileges.add(read);
        superAdminPrivileges.add(write);

        adminPrivileges.add(read);
        adminPrivileges.add(write);

        userPrivileges.add(read);

        superAdminRoles = new HashSet<Role>();
        adminRoles = new HashSet<Role>();
        userRoles = new HashSet<Role>();

        superAdminRole = createRoleIfNotFound(superAdminRole, adminPrivileges);
        adminRole = createRoleIfNotFound(adminRole, adminPrivileges);
        userRole = createRoleIfNotFound(userRole, userPrivileges);

        superAdminRoles.add(superAdminRole);
        superAdminRoles.add(adminRole);
        superAdminRoles.add(userRole);

        adminRoles.add(adminRole);
        adminRoles.add(userRole);

        userRoles.add(userRole);

        userOff.setRoles(userRoles);
        user.setRoles(userRoles);
        adminOff.setRoles(adminRoles);
        admin.setRoles(adminRoles);
        superAdminOff.setRoles(superAdminRoles);
        superAdmin.setRoles(superAdminRoles);

        for (Map.Entry<String, User> el: users.entrySet() ) {
            User user = createUser(el.getValue() );
            createAclSid(user);
            createObjectIdentity(user);
            User olduser =  users.get(el.getKey());
            olduser.setId(user.getId());
//            olduser.setPassword(user.getPassword());
            users.replace(el.getKey(), olduser);
        }

        initLoggedUsers();


//		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    private void initLoggedUsers()
    {
        loggedUser = createFromUser(users.get("user"));
        loggedUserOff = createFromUser(users.get("userOff"));
        loggedAdmin = createFromUser(users.get("admin"));
        loggedAdminOff = createFromUser(users.get("adminOff"));
        loggedSuperAdmin = createFromUser(users.get("superAdmin"));
        loggedSuperAdminOff = createFromUser(users.get("superAdminOff"));
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(Privilege privilege) {

        Privilege privilegeDb = privilegeRepository.findByName(privilege.getName());
        if (privilegeDb == null) {
            privilegeDb = new Privilege(privilege.getName());
            privilegeRepository.save(privilegeDb);

        }
        return privilegeDb;
    }

    @Transactional
    private Role createRoleIfNotFound(
            Role role, Set<Privilege> privileges) {
        Role roleDb = roleRepository.findByName(role.getName());
        if (roleDb == null) {
            roleDb = new Role(role.getName());
            roleDb.setPrivileges( privileges);
            roleRepository.save(roleDb);
        }

        return roleDb;
    }

    @Transactional
    private User createUser(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return user;
    }

    @Transactional
    private AclSecurityID createAclSid(User user)
    {
        AclSecurityID aclSecurityID = new AclSecurityID();
        aclSecurityID.setPrincipal(true);
        aclSecurityID.setSid(user.getEmail());
        aclSecurityIDRepository.save(aclSecurityID);

        return aclSecurityID;
    }

    @Transactional
    private AclObjectIdentity createObjectIdentity(User user)
    {
        AclObjectIdentity aclObjectIdentity = new AclObjectIdentity();

        AclClass userClass = aclClassRepository.findByClassField(user.getClass().getName());
        AclSecurityID sid = aclSecurityIDRepository.findBySid(user.getEmail());

        aclObjectIdentity.setAclClass(userClass);
        aclObjectIdentity.setObjectId(String.valueOf(user.getId()));
        aclObjectIdentity.setOwner(sid);

        aclObjectIdentityRepository.save(aclObjectIdentity);

        return aclObjectIdentity;
    }

    private org.springframework.security.core.userdetails.UserDetails createFromUser(User user)
    {
        return new CustomUserDetails(user);
    }

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        userRepository = testContext.getApplicationContext().getBean(UserRepository.class);
        roleRepository = testContext.getApplicationContext().getBean(RoleRepository.class);
        privilegeRepository = testContext.getApplicationContext().getBean(PrivilegeRepository.class);
        aclSecurityIDRepository = testContext.getApplicationContext().getBean(AclSecurityIDRepository.class);
        aclClassRepository = testContext.getApplicationContext().getBean(AclClassRepository.class);
        aclObjectIdentityRepository = testContext.getApplicationContext().getBean(AclObjectIdentityRepository.class);
        passwordEncoder = testContext.getApplicationContext().getBean(PasswordEncoder.class);
        setup();
    }
}