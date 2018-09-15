package com.example.demo.main.controller;

import com.example.demo.main.model.Book;
import com.example.demo.main.repository.BookRepository;
import com.example.demo.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.util.List;

@Controller
public class MainController {

    private MutableAclService aclService;

    private BookRepository bookRepository;

    @Autowired
    public MainController(MutableAclService aclService, BookRepository bookRepository) {
        this.aclService = aclService;
        this.bookRepository = bookRepository;
    }


    @RequestMapping(path = "/")
    public String home()
    {
        return "home";
    }


    @RequestMapping(path = "/acl/{id}")
    @Transactional
    public String acl(@PathVariable final Long id)
    {

        // Prepare the information we'd like in our access control entry (ACE)
//        allBook().stream().forEach(el -> {
            ObjectIdentity oi = new ObjectIdentityImpl(User.class, id);
//            Sid sid = new PrincipalSid("sadm");
//            Permission p = BasePermission.ADMINISTRATION;

// Create or update the relevant ACL
            MutableAcl acl = null;
            try {
                acl = (MutableAcl) aclService.readAclById(oi);
            } catch (NotFoundException nfe) {
                acl = aclService.createAcl(oi);
            }
System.out.println(acl.getEntries());
// Now grant some permissions via an access control entry (ACE)
//            acl.insertAce(acl.getEntries().size(), p, sid, true);
//            aclService.updateAcl(acl);
//        });
        return "acl";
    }

    private List<Book> allBook()
    {
        return bookRepository.findAll();
    }
}
