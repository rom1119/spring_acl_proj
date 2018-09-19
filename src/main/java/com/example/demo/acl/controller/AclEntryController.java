package com.example.demo.acl.controller;

import com.example.demo.acl.model.AclEntry;
import com.example.demo.acl.model.AclObjectIdentity;
import com.example.demo.acl.model.AclSecurityID;
import com.example.demo.acl.repository.AclEntryRepository;
import com.example.demo.acl.repository.AclObjectIdentityRepository;
import com.example.demo.acl.repository.AclSecurityIDRepository;
import com.example.demo.user.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.model.MutableAclService;
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
@RequestMapping(path = AclEntryController.mainPath)
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AclEntryController {

    private static final String pathToViews = "acl/aclEntry/";
    public static final String mainPath = "acl_entry";

    private AclEntryRepository aclEntryRepository;

    private AclObjectIdentityRepository aclObjectIdentityRepository;

    private AclSecurityIDRepository aclSecurityIDRepository;

    private MutableAclService aclService;

    @Autowired
    public AclEntryController(AclEntryRepository aclEntryRepository, AclObjectIdentityRepository aclObjectIdentityRepository, AclSecurityIDRepository aclSecurityIDRepository, MutableAclService aclService) {
        this.aclEntryRepository = aclEntryRepository;
        this.aclObjectIdentityRepository = aclObjectIdentityRepository;
        this.aclSecurityIDRepository = aclSecurityIDRepository;
        this.aclService = aclService;
    }

    @RequestMapping(path = "/acl_object_identity/{aclObjectIdentityId}", method = RequestMethod.GET)
    public String getAllByAclObjectIdentity(@PathVariable Long aclObjectIdentityId, Model model)
    {
        AclObjectIdentity aclObjectIdentity = aclObjectIdentityRepository.findById(aclObjectIdentityId).get();

        if (aclObjectIdentity == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }
        model.addAttribute("entities", aclObjectIdentity.getEntries());

        return pathToView("list");
    }


    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getOne(@PathVariable Long id, Model model)
    {
        Optional<AclEntry> entity = aclEntryRepository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        model.addAttribute("entity", entity);

        return pathToView("show");
    }

    @RequestMapping(path = "/{id}/new", method = RequestMethod.GET)
    public String createView(Model model)
    {
        AclEntry entity = new AclEntry();

        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        List<AclSecurityID> aclSecurityIDList = aclSecurityIDRepository.findAll();

        model.addAttribute("aclObjectIdentityList", aclObjectIdentityList);
        model.addAttribute("aclSecurityIDList", aclSecurityIDList);
        model.addAttribute("entity", entity);

        return pathToView("new");
    }

    @PreAuthorize("hasPermission(#entity, 'AUDIT')")
    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public String createProccess(@Param("entity") @Valid @ModelAttribute AclEntry entity,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model)
    {
        if (result.hasErrors()) {
            model.addAttribute("entity", entity);
            return pathToView("new");
        }

        aclEntryRepository.save(entity);
        redirectAttributes.addFlashAttribute("new", true);
//        redirectAttributes.addFlashAttribute("name", aclObjectIdentity.ge());
        return redirectToIndex();
    }

    @RequestMapping(path = "/{id}/edit", method = RequestMethod.GET)
    public String editView(@PathVariable int id, Model model)
    {
        AclEntry entity = aclEntryRepository.findById((long) id).get();

        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        List<AclSecurityID> aclSecurityIDList = aclSecurityIDRepository.findAll();

        model.addAttribute("aclObjectIdentityList", aclObjectIdentityList);
        model.addAttribute("aclSecurityIDList", aclSecurityIDList);
        model.addAttribute("entity", entity);

        return pathToView("edit");
    }

    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    public String editProccess(@Param("entity") @Valid @ModelAttribute AclEntry entity,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model)
    {

        if (result.hasErrors()) {
            model.addAttribute("entity", entity);
            return pathToView("edit");
        }

        aclEntryRepository.save(entity);
        redirectAttributes.addFlashAttribute("edited", true);
//        redirectAttributes.addFlashAttribute("name", aclObjectIdentity.ge());
        return redirectToIndex();
    }

    @PreAuthorize("hasPermission(#id, 'User', 'AUDIT')")
    @RequestMapping(path = "/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable Long id, RedirectAttributes attributes) throws IOException {
        AclEntry entity = aclEntryRepository.findById(id).get();
        if (entity == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }
        aclEntryRepository.delete(entity);


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
