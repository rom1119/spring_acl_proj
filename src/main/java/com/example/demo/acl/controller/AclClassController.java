package com.example.demo.acl.controller;

import com.example.demo.acl.model.AclClass;
import com.example.demo.acl.repository.AclClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(path = "acl_class")
public class AclClassController {

    private static final String pathToViews = "acl/aclClass/";

    private AclClassRepository aclClassRepository;

    @Autowired
    public AclClassController(AclClassRepository aclClassRepository) {
        this.aclClassRepository = aclClassRepository;
    }

    @RequestMapping( method = RequestMethod.GET)
    public String getAll(Model model)
    {
        List<AclClass> list = aclClassRepository.findAll();
        model.addAttribute("entities", list);

        return pathToView("list");
    }

    private String pathToView(String view)
    {
        return pathToViews + view;
    }
}
