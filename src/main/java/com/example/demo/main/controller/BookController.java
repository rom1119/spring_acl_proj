package com.example.demo.main.controller;

import com.example.demo.acl.controller.AbstractAclController;
import com.example.demo.main.form.SearchForm;
import com.example.demo.main.model.Book;
import com.example.demo.main.repository.BookRepository;
import com.example.demo.acl.service.CustomAclService;
import com.example.demo.main.service.BookService;
import com.example.demo.user.exception.ResourceNotFoundException;
import com.example.demo.user.model.CustomUserDetails;
import com.example.demo.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

@Controller
@RequestMapping(path = BookController.mainPath)
@PropertySource("classpath:application.properties")
public class BookController extends AbstractAclController<Book> {

    protected static final String pathToViews = "main/book/";
    public static final String mainPath = "book";
    private static final int DEFAULT_PAGE_NUMBER = 0;

    @Value( "${app.availableCountPages}" )
    private String AVAILABLE_COUNT_PAGES;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CustomAclService aclService;

    @Autowired
    private BookService bookService;

    @RequestMapping( method = RequestMethod.GET)
    public String getBooks(Model model,
//               @SortDefault.SortDefaults({
//                       @SortDefault(sort = "name", direction = Sort.Direction.ASC),
//               })
               @Valid @ModelAttribute SearchForm searchForm,
               @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable pageable
                )
    {
        Page<Book> books = null;
        if (searchForm.getTerm() == null) {
            books = bookRepository.findAll(pageable);
        } else {
            books = bookRepository.findBySearchTerm(searchForm.getTerm(), pageable);
        }

        model.addAttribute("entities", books);
        model.addAttribute("countPages", books.getTotalPages());
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("availableCountPages", AVAILABLE_COUNT_PAGES.split(","));

        return pathToView("list");
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getOne(@PathVariable Long id, Model model)
    {
        Optional<Book> user = Optional.ofNullable(bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono strony")));

        List<AccessControlEntry> aclEntries = aclService.getAclEntries(user.get().getClass(), user.get().getId());
//        aclEntries.get(0).
        model.addAttribute("entity", user.get());
        model.addAttribute("aclEntryList", aclEntries);
        return pathToView("show");
    }

    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @RequestMapping(path = "/new", method = RequestMethod.GET)
    public String createView(Model model)
    {
        Book entity = new Book();

        model.addAttribute("entity", entity);

        return pathToView("new");
    }

    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public String createProccess(@Param("entity") @Valid @ModelAttribute Book entity,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model,
                                 Authentication authentication
                        )
    {
        if (result.hasErrors()) {
            model.addAttribute("entity", entity);
            return pathToView("new");
        }

        bookRepository.save(entity);
        User user = ((CustomUserDetails)authentication.getPrincipal()).getUser();
        aclService.createAclWithUserSid(entity.getClass(), entity.getId(), user);

        redirectAttributes.addFlashAttribute("new", true);
//        redirectAttributes.addFlashAttribute("name", aclObjectIdentity.ge());
        return redirectToIndex();
    }


    //    @PreAuthorize("hasPermission(returnObject, 'WRITE')")
    @RequestMapping(path = "/{id}/edit", method = RequestMethod.GET)
    public String editView(@PathVariable int id, Model model)
    {
        Book entity = bookService.findByIdToEdit((long) id);
        model.addAttribute("entity", entity);

        return pathToView("edit");
    }

//    @PreAuthorize("hasPermission(#entity, 'ADMINISTRATION')")
    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    public String editProccess(@Param("entity") @ModelAttribute Book entity, RedirectAttributes redirectAttributes)
    {
        bookService.findByIdToEdit(entity.getId());

        bookRepository.save(entity);
        redirectAttributes.addFlashAttribute("edited", true);
        redirectAttributes.addFlashAttribute("name", entity.getName());
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
