package com.example.demo.user.controller;

import com.example.demo.acl.service.CustomAclService;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.exception.ResourceNotFoundException;
import com.example.demo.user.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@PreAuthorize("hasRole('SUPER_ADMIN')")
@Controller
@RequestMapping(path=RoleController.mainPath)
public class RoleController {

    private static final String pathToViews = "user/role/";
    public static final String mainPath = "role";

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomAclService aclService;


    @RequestMapping(method = RequestMethod.GET)
    public String getAll( Model model)
    {
        List<Role> list = roleRepository.findAll();
        model.addAttribute("entities", list);
        return pathToView("list");

    }


    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getOne(@PathVariable Long id, Model model)
    {
        Optional<Role> role = roleRepository.findById(id);
        if (role == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        model.addAttribute("entity", role);
        return pathToView("show");
    }


    @RequestMapping(path = "/{id}/new", method = RequestMethod.GET)
    public String createView(Model model)
    {
        Role entity = new Role();
        model.addAttribute("entity", entity);

        return pathToView("new");
    }

    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public String createProccess( @Valid @ModelAttribute Role entity,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model)
    {
        if (result.hasErrors()) {
            model.addAttribute("entity", entity);
            return pathToView("new");
        }

        roleRepository.save(entity);
        aclService.createAclWithAuthoritySid(entity.getClass(), entity.getId(), entity);

        redirectAttributes.addFlashAttribute("save", true);
        redirectAttributes.addFlashAttribute("name", entity.getName());
        return redirectToIndex();
    }


    @RequestMapping(path = "/{id}/edit", method = RequestMethod.GET)
    public String editView(@Param("id") @PathVariable Long id, Model model)
    {
        Optional<Role> role = roleRepository.findById(id);
        if (role == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        model.addAttribute("entity", role);
        return pathToView("edit");
    }

    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    public String editProccess( @Valid @ModelAttribute Role role,
                               BindingResult result,
                               Model model,
                               RedirectAttributes attributes) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("entity", role);
            for(ObjectError error : result.getAllErrors()){
//                System.out.println(error.toString() + ": " + error.getDefaultMessage());
            }
            return pathToView("edit");
        }
        roleRepository.save(role);

        attributes.addFlashAttribute("save", true);
        attributes.addFlashAttribute("name", role.getName());
        return redirectToIndex();


    }

    @RequestMapping(path = "/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable Long id, RedirectAttributes attributes) throws IOException {

        Optional<Role> role = roleRepository.findById(id);
        if (role == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        roleRepository.delete(role.get());


        attributes.addFlashAttribute("remove", true);
        attributes.addFlashAttribute("name", role.get().getName());
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
