package com.example.demo.acl.fixtures;

import com.example.demo.acl.model.AclClass;
import com.example.demo.acl.repository.AclClassRepository;
import com.example.demo.main.fixtures.FixturesInterface;
import com.example.demo.main.model.Book;
import com.example.demo.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Component
@DependsOn("initialBookData")
public class InitialAclClassData implements FixturesInterface {

    @Autowired
    private AclClassRepository aclClassRepository;

    @PostConstruct
    @Transactional
    public void init()
    {
        AclClass aclClass = new AclClass();
        aclClass.setClassField(Book.class.getName());
        aclClass.setName(Book.class.getName());
        aclClass.setClassIdType("java.lang.Long");

        aclClassRepository.save(aclClass);

        AclClass aclClassu = new AclClass();
        aclClassu.setClassField(User.class.getName());
        aclClassu.setName(User.class.getName());
        aclClassu.setClassIdType("java.lang.String");

        aclClassRepository.save(aclClassu);
    }
}
