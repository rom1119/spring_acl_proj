package com.example.demo.main.fixtures;

import com.example.demo.main.model.Book;
import com.example.demo.main.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Component
public class InitialBookData implements FixturesInterface {

    private Book[] books = {
           new Book("Pan Tadeusz", "Adam Mickiewicz", "Phasellus quis dignissim leo. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean sit amet neque gravida, scelerisque tellus id, aliquam massa. Nunc in nisi dolor. Morbi porta elementum nulla, at blandit est. Vivamus purus nisi, faucibus vitae vestibulum vitae, consectetur in risus. Vivamus tincidunt magna quam, vitae eleifend lectus dapibus id. Aliquam dui eros, elementum sit amet erat at, tincidunt porttitor est. Aenean eget ornare nisl."),
            new Book("Pan Tadeusz 1", "Adam Mickiewicz 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent in euismod lacus, in ultricies tortor. Quisque accumsan massa sapien, at accumsan tellus vulputate et. Duis efficitur lobortis magna, ut bibendum lacus facilisis et. Nulla facilisi. In sem turpis, venenatis posuere tempus sit amet, vestibulum non leo. Pellentesque elementum tincidunt ante sit amet volutpat. Sed tempus nisl ac luctus rutrum. Quisque tempor purus nec dolor fringilla, non volutpat leo convallis. Curabitur rutrum augue enim, nec viverra enim lobortis vel. Nam id urna volutpat turpis consequat tempus quis eu justo. Etiam non velit nec erat imperdiet scelerisque vitae at orci. Nam ante eros, finibus eu nisl a, tempor molestie ante. Etiam a ipsum ut magna scelerisque malesuada ut vulputate lorem."),
            new Book("Pan Tadeusz 2", "Adam Mickiewicz 2", "In non dui quam. Phasellus imperdiet felis est, eu euismod justo placerat id. Praesent nec eros elit. Vestibulum et iaculis diam. Suspendisse laoreet felis metus, convallis tincidunt ex imperdiet sed. Maecenas id ex lorem. In hac habitasse platea dictumst. Phasellus dui nisl, placerat vel sem tempor, vestibulum euismod leo. Duis commodo gravida odio, id aliquam nibh tincidunt non. Nam a sem lorem. Curabitur vel vehicula libero."),
            new Book("Pan Tadeusz 3" , "Adam Mickiewicz 3", "Phasellus quis dignissim leo. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean sit amet neque gravida, scelerisque tellus id, aliquam massa. Nunc in nisi dolor. Morbi porta elementum nulla, at blandit est. Vivamus purus nisi, faucibus vitae vestibulum vitae, consectetur in risus. Vivamus tincidunt magna quam, vitae eleifend lectus dapibus id. Aliquam dui eros, elementum sit amet erat at, tincidunt porttitor est. Aenean eget ornare nisl."),
            new Book("Pan Tadeusz 4", "Adam Mickiewicz 4", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent in euismod lacus, in ultricies tortor. Quisque accumsan massa sapien, at accumsan tellus vulputate et. Duis efficitur lobortis magna, ut bibendum lacus facilisis et. Nulla facilisi. In sem turpis, venenatis posuere tempus sit amet, vestibulum non leo. Pellentesque elementum tincidunt ante sit amet volutpat. Sed tempus nisl ac luctus rutrum. Quisque tempor purus nec dolor fringilla, non volutpat leo convallis. Curabitur rutrum augue enim, nec viverra enim lobortis vel. Nam id urna volutpat turpis consequat tempus quis eu justo. Etiam non velit nec erat imperdiet scelerisque vitae at orci. Nam ante eros, finibus eu nisl a, tempor molestie ante. Etiam a ipsum ut magna scelerisque malesuada ut vulputate lorem."),
            new Book("Pan Tadeusz 5", "Adam Mickiewicz 5", "In non dui quam. Phasellus imperdiet felis est, eu euismod justo placerat id. Praesent nec eros elit. Vestibulum et iaculis diam. Suspendisse laoreet felis metus, convallis tincidunt ex imperdiet sed. Maecenas id ex lorem. In hac habitasse platea dictumst. Phasellus dui nisl, placerat vel sem tempor, vestibulum euismod leo. Duis commodo gravida odio, id aliquam nibh tincidunt non. Nam a sem lorem. Curabitur vel vehicula libero."),
            new Book("Pan Tadeusz 6", "Adam Mickiewicz 6", "In non dui quam. Phasellus imperdiet felis est, eu euismod justo placerat id. Praesent nec eros elit. Vestibulum et iaculis diam. Suspendisse laoreet felis metus, convallis tincidunt ex imperdiet sed. Maecenas id ex lorem. In hac habitasse platea dictumst. Phasellus dui nisl, placerat vel sem tempor, vestibulum euismod leo. Duis commodo gravida odio, id aliquam nibh tincidunt non. Nam a sem lorem. Curabitur vel vehicula libero.")
    };

    @Autowired
    private BookRepository bookRepository;

    @PostConstruct
    @Transactional
    public void init()
    {
        Arrays.stream(books).forEach(book ->  bookRepository.save(book));
    }

}
