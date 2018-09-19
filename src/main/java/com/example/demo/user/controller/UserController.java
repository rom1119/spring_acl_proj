package com.example.demo.user.controller;

import com.example.demo.acl.controller.AbstractAclController;
import com.example.demo.acl.model.AclEntry;
import com.example.demo.acl.model.AclEntryDto;
import com.example.demo.acl.model.AclSecurityID;
import com.example.demo.acl.repository.AclEntryRepository;
import com.example.demo.acl.repository.AclSecurityIDRepository;
import com.example.demo.acl.service.AclEntryService;
import com.example.demo.acl.service.AclObjectDomainService;
import com.example.demo.acl.service.CustomAclService;
import com.example.demo.main.validation.group.Created;
import com.example.demo.main.validation.group.Edited;
import com.example.demo.main.validation.group.PasswordChange;
import com.example.demo.user.exception.ResourceNotFoundException;
import com.example.demo.user.model.Role;
import com.example.demo.user.model.User;
import com.example.demo.user.model.UserDto;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.main.service.StorageManager;
import com.example.demo.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path=UserController.mainPath)
public class UserController extends AbstractAclController<User> {

    protected static final String pathToViews = "user/user/";
    public static final String mainPath = "user";

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RoleRepository roleRepository;

    protected ModelMapper modelMapper;

    @Autowired
    protected StorageManager storageService;

    @Autowired
    protected CustomAclService aclService;

    protected UserService userService;


    @Autowired
    public UserController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);

        this.userService = userService;

    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAll( Model model) throws IllegalAccessException {
        List<User> users = userService.findAll();
        for (User user : users) {
            List<User> oneToEdit = userService.getOneToEdit(user);
            System.out.println(oneToEdit.size());

        }

        model.addAttribute("entities", users);
        return pathToView("list");

    }


    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getOne(@PathVariable Long id, Model model)
    {
        User user = userService.findByIdToView(id);
        if (user == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        List<AccessControlEntry> aclEntries = aclService.getAclEntries(user.getClass(), user.getId());
//        aclEntries.get(0).
        model.addAttribute("entity", user);
        model.addAttribute("aclEntryList", aclEntries);
        return pathToView("show");
    }

    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @RequestMapping(path = "/{id}/new", method = RequestMethod.GET)
    public String createView(Model model)
    {
        UserDto entity = new UserDto();
        model.addAttribute("entity", entity);

        return pathToView("new");
    }

    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public String createProccess(@Param("entity") @Valid @ModelAttribute UserDto entity,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("entity", entity);
            return pathToView("new");
        }

        User user = modelMapper.map(entity, User.class);
        User userDb = userRepository.findById(user.getId()).get();
        user.getUserDetails().setFileName(userDb.getUserDetails().getFileName());

        storageService.updateFile(user.getUserDetails());
        userRepository.save(user);

        aclService.createAclWithUserSid(user.getClass(), user.getId(), user);

        redirectAttributes.addFlashAttribute("save", true);
        redirectAttributes.addFlashAttribute("userName", user.getEmail());
        return redirectToIndex();
    }

    @RequestMapping(path = "/{id}/edit", method = RequestMethod.GET)
    public String editView(@Param("id") @PathVariable Long id, Model model)
    {
        User user = userService.findByIdToEdit(id);
        if (user == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        UserDto userDto = modelMapper.map(user, UserDto.class);

        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("user", userDto);
        return pathToView("edit");
    }

    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    public String editProccess(@Param("user") @Valid @ModelAttribute("user") UserDto userDto,
                       BindingResult result,
                       Model model,
                       RedirectAttributes attributes) throws IOException {
        User userDb = userService.findByIdToEdit(userDto.getId());
        if (userDb == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        if (result.hasErrors()) {
            List<Role> roles = roleRepository.findAll();
            model.addAttribute("roles", roles);
            model.addAttribute("user", userDto);
            return pathToView("edit");
        }

        User user = modelMapper.map(userDto, User.class);
        user.getUserDetails().setFileName(userDb.getUserDetails().getFileName());

        storageService.updateFile(user.getUserDetails());
        userRepository.save(user);

        attributes.addFlashAttribute("save", true);
        attributes.addFlashAttribute("userName", userDto.getEmail());
        return redirectToIndex();


    }

    @RequestMapping(path = "/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable Long id, RedirectAttributes attributes) throws IOException {
        System.out.println("raz");
        User user = userService.findByIdToDelete(id);
        System.out.println("dwa");
        if (user == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }
        storageService.deleteFile(user.getUserDetails());
        userRepository.delete(user);


        attributes.addFlashAttribute("remove", true);
        attributes.addFlashAttribute("userName", user.getEmail());
        return redirectToIndex();
    }

    @RequestMapping(path = "/{id}/change_password", method = RequestMethod.GET)
    public String changePasswordView(@PathVariable final Long id, Model model)
    {
        User user = userService.findByIdToChangePassword(id);
        if (user == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setVersion(user.getVersion());

        model.addAttribute("entity", userDto);

        return pathToView("changePassword");
    }

    @RequestMapping(path = "/change_password", method = RequestMethod.POST)
    public String changePasswordProccess(
            @Validated(PasswordChange.class) @ModelAttribute("entity") final UserDto userDto,
             BindingResult result,
             RedirectAttributes attributes,
             Model model)
    {
        User user = userService.findByIdToChangePassword(userDto.getId());
        if (user == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");

        }

        result.getAllErrors().stream().forEach(el -> {
            System.out.println(el.toString());
        });

        if (result.hasErrors()) {
            model.addAttribute("entity", userDto);
            return pathToView("changePassword");
        }

        userService.changePassword(userDto);

        attributes.addAttribute("passwordChanges", true);
        attributes.addAttribute("userName", user.getEmail());

        return redirectToIndex();
    }

    @Override
    protected String pathToView(String view)
    {
        return getPathToViews() + view;
    }

    @Override
    protected String redirectToIndex()
    {
        return redirectTo(mainPath);
    }

    @Override
    protected String redirectTo(String partPath)
    {
        return "redirect:/" + partPath;
    }

    @Override
    protected String getMainPath()
    {
        return mainPath;
    }

    @Override
    protected String getPathToViews()
    {
        return pathToViews;
    }
}
