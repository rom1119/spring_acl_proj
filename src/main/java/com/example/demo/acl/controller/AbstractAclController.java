package com.example.demo.acl.controller;

import com.example.demo.acl.model.AclEntry;
import com.example.demo.acl.model.AclEntryDto;
import com.example.demo.acl.model.AclResourceInterface;
import com.example.demo.acl.model.AclSecurityID;
import com.example.demo.acl.repository.AclEntryRepository;
import com.example.demo.acl.repository.AclSecurityIDRepository;
import com.example.demo.acl.service.AclEntryService;
import com.example.demo.acl.service.AclObjectDomainService;
import com.example.demo.acl.service.CustomAclService;
import com.example.demo.acl.service.IAclService;
import com.example.demo.main.model.ResourceInterface;
import com.example.demo.main.validation.group.Created;
import com.example.demo.main.validation.group.Edited;
import com.example.demo.user.exception.ResourceNotFoundException;
import com.example.demo.user.model.User;
import com.example.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class AbstractAclController<T extends AclResourceInterface> {

    protected static final String pathToViews = "";
    public static final String mainPath = "";

    @Autowired
    protected IAclService<T> securedEntityService;

    @Autowired
    protected CustomAclService aclService;

    @Autowired
    protected AclSecurityIDRepository securityIDRepository;

    @Autowired
    protected AclEntryRepository aclEntryRepository;

    @Autowired
    protected AclEntryService aclEntryService;

    @Autowired
    protected AclObjectDomainService aclObjectDomainService;


    @RequestMapping(path = "/{id}/acl_entry/new", method = RequestMethod.GET)
    public String addAclEntryView(@Param("id") @PathVariable Long id, Model model) throws IllegalAccessException {
        T securedEntity = securedEntityService.findByIdToAdministration(id);
        if (securedEntity == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");

        }

        AclEntry aclEntry = new AclEntry();
        Map<String, Permission> availablePermission = aclService.getAvailablePermission();
        List<AclSecurityID> securityIDList = securityIDRepository.findAll();

        model.addAttribute("targetSecuredObjectId", securedEntity.getId());
        model.addAttribute("entity", aclEntry);
        model.addAttribute("availablePermission", availablePermission);
        model.addAttribute("securityIDList", securityIDList);
        return pathToView("aclEntry/new");
    }

    @RequestMapping(path = "/{id}/acl_entry/new", method = RequestMethod.POST)
    public String addAclEntryProccess(
            @PathVariable final Long id,
            @Param("entity") @Validated(Created.class) @ModelAttribute("entity") AclEntry entity,
            BindingResult result,
            Model model,
            RedirectAttributes attributes,
            Authentication authentication
    ) throws IOException, IllegalAccessException {

        if (result.hasErrors()) {
            Map<String, Permission> availablePermission = aclService.getAvailablePermission();
            List<AclSecurityID> securityIDList = securityIDRepository.findAll();

            model.addAttribute("availablePermission", availablePermission);
            model.addAttribute("securityIDList", securityIDList);
            model.addAttribute("entity", entity);
            model.addAttribute("targetSecuredObjectId", id);
            return pathToView("aclEntry/new");
        }

        AclResourceInterface securedEntity = securedEntityService.findByIdToAdministration(id);

        AccessControlEntry aclEntry = aclService.createAclEntry(entity, securedEntity.getClass(), id);

//        System.out.println(user.getId());

        attributes.addFlashAttribute("save", true);
        attributes.addFlashAttribute("aclEntryName", aclService.getSidName(aclEntry));
        attributes.addFlashAttribute("aclEntryPermission", aclService.getPermissionName(aclEntry.getPermission()));
        attributes.addFlashAttribute("isUser", aclService.isUserSid(aclEntry));

        attributes.addAttribute("id", id);
        return redirectTo(getMainPath() + "/{id}");


    }

    @RequestMapping(path = "/{targetSecuredObjectId}/acl_entry/{aclEntryIndex}/edit", method = RequestMethod.GET)
    public String editAclEntryView(
            @Param("targetSecuredObjectId") @PathVariable Long targetSecuredObjectId,
            @Param("aclEntryIndex") @PathVariable int aclEntryIndex,
            Model model)
            throws Throwable {
        AclResourceInterface securedEntity = securedEntityService.findByIdToAdministration(targetSecuredObjectId);

        if (securedEntity == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        aclObjectDomainService.checkAccessObjectDomain(securedEntity);

        AccessControlEntry aclEntry = aclEntryService.getAce(securedEntity, aclEntryIndex)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono strony"));
        AclEntryDto aclEntryDto = aclEntryService.prepareToEdit(aclEntry);

        Map<String, Permission> availablePermission = aclService.getAvailablePermission();
        List<AclSecurityID> securityIDList = securityIDRepository.findAll();

        model.addAttribute("userId", securedEntity.getId());
        model.addAttribute("aclEntryIndex", aclEntryIndex);
        model.addAttribute("entity", aclEntryDto);
        model.addAttribute("availablePermission", availablePermission);
        model.addAttribute("securityIDList", securityIDList);
        return pathToView("aclEntry/edit");
    }

    @RequestMapping(path = "/{targetSecuredObjectId}/acl_entry/{aclEntryIndex}/edit", method = RequestMethod.POST)
    public String editAclEntryProccess(
            @PathVariable final Long targetSecuredObjectId,
            @PathVariable final int aclEntryIndex,
            @Param("entity") @Validated(Edited.class) @ModelAttribute("entity") AclEntryDto entity,
            BindingResult result,
            Model model,
            RedirectAttributes attributes
    ) throws IOException, IllegalAccessException {

        AclResourceInterface securedEntity = securedEntityService.findByIdToAdministration(targetSecuredObjectId);

        if (securedEntity == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        aclObjectDomainService.checkAccessObjectDomain(securedEntity);

        if (result.hasErrors()) {
            Map<String, Permission> availablePermission = aclService.getAvailablePermission();
            List<AclSecurityID> securityIDList = securityIDRepository.findAll();

            model.addAttribute("userId", securedEntity.getId());
            model.addAttribute("aclEntryIndex", aclEntryIndex);
            model.addAttribute("availablePermission", availablePermission);
            model.addAttribute("securityIDList", securityIDList);
            model.addAttribute("entity", entity);
            return pathToView("aclEntry/edit");
        }

        aclEntryService.getAce(securedEntity, aclEntryIndex)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono strony"));
        AclEntry aclEntry = aclEntryService.prepareFromEdit(entity);


        AccessControlEntry accessControlEntry = aclService.updateAclEntry(aclEntry, aclEntryIndex, securedEntity);

//        System.out.println(user.getId());

        attributes.addFlashAttribute("save", true);
        attributes.addFlashAttribute("aclEntryName", aclService.getSidName(accessControlEntry));
        attributes.addFlashAttribute("aclEntryPermission", aclService.getPermissionName(accessControlEntry.getPermission()));
        attributes.addFlashAttribute("isUser", aclService.isUserSid(accessControlEntry));

        attributes.addAttribute("id", securedEntity.getId());
        return redirectTo(getMainPath() + "/{id}");

    }

    @RequestMapping(path = "/{targetSecuredObjectId}/acl_entry/{aclEntryIndex}/delete", method = RequestMethod.GET)
    public String deleteAclEntry(
            @Param("targetSecuredObjectId") @PathVariable Long targetSecuredObjectId,
            @Param("aclEntryIndex") @PathVariable int aclEntryIndex,
            RedirectAttributes attributes,
            Model model) throws IllegalAccessException {
        AclResourceInterface securedEntity = securedEntityService.findByIdToAdministration(targetSecuredObjectId);

        if (securedEntity == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        aclObjectDomainService.checkAccessObjectDomain(securedEntity);

        AccessControlEntry accessControlEntry = aclService.deleteAclEntry(aclEntryIndex, securedEntity);

        attributes.addFlashAttribute("remove", true);
        attributes.addFlashAttribute("aclEntryName", aclService.getSidName(accessControlEntry));
        attributes.addFlashAttribute("aclEntryPermission", aclService.getPermissionName(accessControlEntry.getPermission()));
        attributes.addFlashAttribute("isUser", aclService.isUserSid(accessControlEntry));

        attributes.addAttribute("id", securedEntity.getId());
        return redirectTo(getMainPath() + "/{id}");
    }

    protected String pathToView(String view)
    {
        return getPathToViews() + view;
    }



    protected String redirectToIndex()
    {
        return redirectTo(this.mainPath);
    }

    protected String redirectTo(String partPath)
    {
        return "redirect:/" + partPath;
    }

    protected String getMainPath()
    {
        return mainPath;
    }

    protected String getPathToViews()
    {
        return pathToViews;
    }
}
