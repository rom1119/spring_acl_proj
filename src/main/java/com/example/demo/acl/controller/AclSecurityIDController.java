package com.example.demo.acl.controller;

import com.example.demo.acl.model.AclSecurityID;
import com.example.demo.acl.repository.AclClassRepository;
import com.example.demo.acl.repository.AclSecurityIDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(path = "acl_security_id")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AclSecurityIDController {

    private AclSecurityIDRepository aclSecurityIDRepository;

    private static final String pathToViews = "acl/aclSecurityID/";

    @Autowired
    public AclSecurityIDController(AclSecurityIDRepository aclSecurityIDRepository) {
        this.aclSecurityIDRepository = aclSecurityIDRepository;
    }

    @RequestMapping( method = RequestMethod.GET)
    public String getAll(Model model)
    {
        List<AclSecurityID> list = aclSecurityIDRepository.findAll();
        model.addAttribute("entities", list);

        return "acl/aclSecurityID/list";
    }
}
