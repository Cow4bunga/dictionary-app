package by.levitsky.spring.dictionaryservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/home")
public class MainPageController {
    @GetMapping()
    public String goHome(@RequestParam (name="name",required = false,defaultValue = "System")
                         String name,
                         Model model){
        model.addAttribute("name",name);
        return "home";
    }
}
