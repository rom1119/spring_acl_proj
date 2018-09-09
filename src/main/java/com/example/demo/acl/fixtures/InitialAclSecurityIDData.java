package com.example.demo.acl.fixtures;

import com.example.demo.acl.model.AclSecurityID;
import com.example.demo.acl.repository.AclSecurityIDRepository;
import com.example.demo.main.fixtures.FixturesInterface;
import com.example.demo.user.repository.PrivilegeRepository;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.model.Privilege;
import com.example.demo.user.model.Role;
import com.example.demo.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;

@Component
@DependsOn("initialUserData")
public class InitialAclSecurityIDData implements FixturesInterface {

    private AclSecurityIDRepository aclSecurityIDRepository;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PrivilegeRepository privilegeRepository;

    @Autowired
    public InitialAclSecurityIDData(AclSecurityIDRepository aclSecurityIDRepository, UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.aclSecurityIDRepository = aclSecurityIDRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @PostConstruct
    @Transactional
    public void init()
    {

        getAllUsers().stream().forEach(el -> {
            AclSecurityID aclClass = new AclSecurityID();
            aclClass.setPrincipal(true);
            aclClass.setSid(el.getEmail());

            aclSecurityIDRepository.save(aclClass);
        });

        getAllRoles().stream().forEach(el -> {
            AclSecurityID aclClass = new AclSecurityID();
            aclClass.setPrincipal(false);
            aclClass.setSid(el.getName());

            aclSecurityIDRepository.save(aclClass);
        });

        getAllPrivileges().stream().forEach(el -> {
            AclSecurityID aclClass = new AclSecurityID();
            aclClass.setPrincipal(false);
            aclClass.setSid(el.getName());

            aclSecurityIDRepository.save(aclClass);
        });


    }

    private List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    private List<Role> getAllRoles()
    {
        return roleRepository.findAll();
    }

    private List<Privilege> getAllPrivileges()
    {
        return privilegeRepository.findAll();
    }
}
