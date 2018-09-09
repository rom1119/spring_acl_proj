package com.example.demo.acl.fixtures;

import com.example.demo.acl.model.AclClass;
import com.example.demo.acl.model.AclObjectIdentity;
import com.example.demo.acl.model.AclSecurityID;
import com.example.demo.acl.repository.AclClassRepository;
import com.example.demo.acl.repository.AclObjectIdentityRepository;
import com.example.demo.acl.repository.AclSecurityIDRepository;
import com.example.demo.main.model.Book;
import com.example.demo.main.repository.BookRepository;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
@DependsOn({"initialUserData", "initialBookData", "initialAclSecurityIDData"})
public class InitialAclObjectIdentityData {

    private BookRepository bookRepository;

    private AclSecurityIDRepository aclSecurityIDRepository;

    private AclClassRepository aclClassRepository;

    private AclObjectIdentityRepository aclObjectIdentityRepository;

    private UserRepository userRepository;

    @Autowired
    public InitialAclObjectIdentityData(BookRepository bookRepository, AclSecurityIDRepository aclSecurityIDRepository, AclClassRepository aclClassRepository, AclObjectIdentityRepository aclObjectIdentityRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.aclSecurityIDRepository = aclSecurityIDRepository;
        this.aclClassRepository = aclClassRepository;
        this.aclObjectIdentityRepository = aclObjectIdentityRepository;
        this.userRepository = userRepository;
    }



    @PostConstruct
    @Transactional
    public void init()
    {

        allBook().stream().forEach(el -> {
            createAclBook(el);
        });

        allUsers().stream().forEach(el -> {
            createAclUser(el);
        });

    }

    private AclObjectIdentity createAclUser(User user)
    {
        AclObjectIdentity aclObjectIdentity = new AclObjectIdentity();
        aclObjectIdentity.setEntriesInheriting(true);

        Optional<AclSecurityID> sid = aclSecurityIDRepository.findById((long) 1);

        aclObjectIdentity.setOwner(sid.get());
        aclObjectIdentity.setObjectId(String.valueOf(user.getId()));

        AclClass aclClass = aclClassRepository.findByClassField(user.getClass().getName());

        aclObjectIdentity.setAclClass(aclClass);

        aclObjectIdentityRepository.save(aclObjectIdentity);

        return aclObjectIdentity;
    }

    private AclObjectIdentity createAclBook(Book book)
    {
        AclObjectIdentity aclObjectIdentity = new AclObjectIdentity();
        aclObjectIdentity.setEntriesInheriting(true);

        Optional<AclSecurityID> sid = aclSecurityIDRepository.findById((long) 1);

        aclObjectIdentity.setOwner(sid.get());
        aclObjectIdentity.setObjectId(String.valueOf(book.getId()));

        AclClass aclClass = aclClassRepository.findByClassField(book.getClass().getName());

        aclObjectIdentity.setAclClass(aclClass);

        aclObjectIdentityRepository.save(aclObjectIdentity);

        return aclObjectIdentity;
    }

    private List<Book> allBook()
    {
        return bookRepository.findAll();
    }
    private List<User> allUsers()
    {
        return userRepository.findAll();
    }

}
