package com.example.demo.main.controller;

import com.example.demo.main.model.Book;
import com.example.demo.main.repository.BookRepository;
import com.example.demo.acl.service.CustomAclService;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = BookController.mainPath)
public class BookController {

    private static final String pathToViews = "main/book/";
    public static final String mainPath = "book";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CustomAclService aclService;


//    @Autowired
//    public BookController(BookRepository bookRepository, AclService aclService) {
//        this.bookRepository = bookRepository;
//        this.asdasd = aclService;
//    }


    @RequestMapping( method = RequestMethod.GET)
    public String getBooks(Model model)
    {
        List<Book> books = bookRepository.findAll();

        model.addAttribute("entities", books);

        return pathToView("list");
    }

    @RequestMapping(path = "/{id}/new", method = RequestMethod.GET)
    public String createView(Model model)
    {
        Book entity = new Book();

        model.addAttribute("entity", entity);

        return pathToView("new");
    }

    @PreAuthorize("hasPermission(#entity, 'AUDIT')")
    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public String createProccess(@Param("entity") @Valid @ModelAttribute Book entity,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model)
    {
        if (result.hasErrors()) {
            model.addAttribute("entity", entity);
            return pathToView("new");
        }

        bookRepository.save(entity);
        aclService.createAcl(entity.getClass(), entity.getId());
        redirectAttributes.addFlashAttribute("new", true);
//        redirectAttributes.addFlashAttribute("name", aclObjectIdentity.ge());
        return redirectToIndex();
    }


    //    @PreAuthorize("hasPermission(returnObject, 'WRITE')")
    @RequestMapping(path = "/{id}/edit", method = RequestMethod.GET)
    public String editView(@PathVariable int id, Model model)
    {
        Optional<Book> entity = bookRepository.findById((long) id);
        model.addAttribute("entity", entity);

        return pathToView("edit");
    }

    @PreAuthorize("hasPermission(#entity, 'ADMINISTRATION')")
    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    public String editProccess(@Param("entity") @ModelAttribute Book entity, RedirectAttributes redirectAttributes)
    {

        bookRepository.save(entity);
        redirectAttributes.addFlashAttribute("edited", true);
        redirectAttributes.addFlashAttribute("name", entity.getName());
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
