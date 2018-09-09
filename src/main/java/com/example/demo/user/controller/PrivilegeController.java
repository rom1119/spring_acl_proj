package com.example.demo.user.controller;

        import com.example.demo.user.repository.PrivilegeRepository;
        import com.example.demo.user.exception.ResourceNotFoundException;
        import com.example.demo.user.model.Privilege;
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

@PreAuthorize("hasRole('SUPER_ADMIN')")
@Controller
@RequestMapping(path=PrivilegeController.mainPath)
public class PrivilegeController {

    private static final String pathToViews = "user/privilege/";
    public static final String mainPath = "privilege";

    @Autowired
    private PrivilegeRepository privilegeRepository;


    @RequestMapping(method = RequestMethod.GET)
    public String getAll( Model model)
    {
        List<Privilege> list = privilegeRepository.findAll();
        model.addAttribute("entities", list);
        return pathToView("list");

    }


    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getOne(@PathVariable Long id, Model model)
    {
        Optional<Privilege> entity = privilegeRepository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        model.addAttribute("entity", entity);
        return pathToView("show");
    }


    @RequestMapping(path = "/{id}/new", method = RequestMethod.GET)
    public String createView(Model model)
    {
        Privilege entity = new Privilege();
        model.addAttribute("entity", entity);

        return pathToView("new");
    }

    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public String createProccess(@Param("entity") @Valid @ModelAttribute Privilege entity,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model)
    {
        if (result.hasErrors()) {
            model.addAttribute("entity", entity);
            return pathToView("new");
        }

        privilegeRepository.save(entity);
        redirectAttributes.addFlashAttribute("save", true);
        redirectAttributes.addFlashAttribute("name", entity.getName());
        return redirectToIndex();
    }


    @RequestMapping(path = "/{id}/edit", method = RequestMethod.GET)
    public String editView(@Param("id") @PathVariable Long id, Model model)
    {
        Optional<Privilege> entity = privilegeRepository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        model.addAttribute("entity", entity);
        return pathToView("edit");
    }

    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    public String editProccess(@Valid @ModelAttribute Privilege entity,
                               BindingResult result,
                               Model model,
                               RedirectAttributes attributes) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("entity", entity);
            return pathToView("edit");
        }
        privilegeRepository.save(entity);

        attributes.addFlashAttribute("save", true);
        attributes.addFlashAttribute("name", entity.getName());
        return redirectToIndex();


    }

    @RequestMapping(path = "/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable Long id, RedirectAttributes attributes) throws IOException {

        Optional<Privilege> entity = privilegeRepository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Nie znaleziono strony");
        }

        privilegeRepository.delete(entity.get());


        attributes.addFlashAttribute("remove", true);
        attributes.addFlashAttribute("name", entity.get().getName());
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

