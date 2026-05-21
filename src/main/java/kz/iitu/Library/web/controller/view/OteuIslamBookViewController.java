package kz.iitu.Library.web.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OteuIslamBookViewController {

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/books/{id}")
    public String bookDetailPage(@PathVariable Long id) {
        return "book-detail";
    }

    @GetMapping("/borrows/my")
    public String myBorrowsPage() {
        return "my-borrows";
    }
}