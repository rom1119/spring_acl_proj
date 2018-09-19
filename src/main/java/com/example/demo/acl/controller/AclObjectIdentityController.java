package com.example.demo.acl.controller;

import com.example.demo.acl.model.AclClass;
import com.example.demo.acl.model.AclObjectIdentity;
import com.example.demo.acl.model.AclSecurityID;
import com.example.demo.acl.repository.AclClassRepository;
import com.example.demo.acl.repository.AclObjectIdentityRepository;
import com.example.demo.acl.repository.AclSecurityIDRepository;
import com.example.demo.user.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = AclObjectIdentityController.mainPath)
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AclObjectIdentityController {

    private static final String pathToViews = "acl/aclObjectIdentity/";
    public static final String mainPath = "acl_object_identity";

    private AclObjectIdentityRepository aclObjectIdentityRepository;

    private AclClassRepository aclClassRepository;

    private AclSecurityIDRepository aclSecurityIDRepository;

    @Autowired
    public AclObjectIdentityController(AclObjectIdentityRepository aclObjectIdentityRepository, AclClassRepository aclClassRepository, AclSecurityIDRepository aclSecurityIDRepository) {
        this.aclObjectIdentityRepository = aclObjectIdentityRepository;
        this.aclClassRepository = aclClassRepository;
        this.aclSecurityIDRepository = aclSecurityIDRepository;
    }

    @RequestMapping( method = RequestMethod.GET)
    public String getAll(Model model)
    {
        List<AclObjectIdentity> list = aclObjectIdentityRepository.findAll();
        model.addAttribute("entities", list);

        return pathToView("list");
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getOne(@PathVariable Long id, Model model)
    {
        Optional<AclObjectIdentity> entity = aclObjectIdentityRepository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        model.addAttribute("entity", entity);

        return pathToView("show");
    }

    @RequestMapping(path = "/new", method = RequestMethod.GET)
    public String createView(Model model)
    {
        AclObjectIdentity entity = new AclObjectIdentity();

        List<AclClass> aclClassList = aclClassRepository.findAll();
        List<AclSecurityID> aclSecurityIDList = aclSecurityIDRepository.findAll();

        model.addAttribute("aclClassList", aclClassList);
        model.addAttribute("aclSecurityIDList", aclSecurityIDList);
        model.addAttribute("entity", entity);

        return pathToView("new");
    }

    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public String createProccess(@Param("aclObjectIdentity") @Valid @ModelAttribute AclObjectIdentity entity,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model)
    {
        if (result.hasErrors()) {
            model.addAttribute("entity", entity);
            return pathToView("new");
        }

        aclObjectIdentityRepository.save(entity);
        redirectAttributes.addFlashAttribute("new", true);
//        redirectAttributes.addFlashAttribute("name", aclObjectIdentity.ge());
        return redirectToIndex();
    }

    @RequestMapping(path = "/{id}/edit", method = RequestMethod.GET)
    public String editView(@PathVariable int id, Model model)
    {
        Optional<AclObjectIdentity> entity = aclObjectIdentityRepository.findById((long) id);

        List<AclClass> aclClassList = aclClassRepository.findAll();
        List<AclSecurityID> aclSecurityIDList = aclSecurityIDRepository.findAll();

        model.addAttribute("aclClassList", aclClassList);
        model.addAttribute("aclSecurityIDList", aclSecurityIDList);
        model.addAttribute("entity", entity);

        return pathToView("edit");
    }

    @PreAuthorize("hasPermission(#aclObjectIdentity, 'AUDIT')")
    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    public String editProccess(@Param("aclObjectIdentity") @Valid @ModelAttribute AclObjectIdentity entity,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model)
    {

        if (result.hasErrors()) {
            model.addAttribute("entity", entity);
            return pathToView("edit");
        }

        aclObjectIdentityRepository.save(entity);
        redirectAttributes.addFlashAttribute("edited", true);
//        redirectAttributes.addFlashAttribute("name", aclObjectIdentity.ge());
        return redirectToIndex();
    }

    @PreAuthorize("hasPermission(#id, 'User', 'AUDIT')")
    @RequestMapping(path = "/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable Long id, RedirectAttributes attributes) throws IOException {
        Optional<AclObjectIdentity> entity = aclObjectIdentityRepository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }
        aclObjectIdentityRepository.delete(entity.get());


        attributes.addFlashAttribute("removed", true);
//        attributes.addFlashAttribute("userEmail", entity.getEmail());
        return redirectToIndex();
    }

    private String pathToView(String view)
    {
        return pathToViews + view;
    }

    private String redirectToIndex()
    {
        return "redirect:/" + mainPath;
    }
}
