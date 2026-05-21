package kz.iitu.Library.web.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OteuIslamProfileViewController {

    @GetMapping("/profile")
    public String profilePage() {
        return "profile";
    }
}