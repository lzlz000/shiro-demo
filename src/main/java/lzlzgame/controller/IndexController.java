package lzlzgame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController{

    @GetMapping({"/","/index"})
    public ModelAndView index() {
        return new ModelAndView("index")
                .addObject("hello", "hello world!");
    }
    @GetMapping({"/admin"})
    public String admin() {
        return "admin";
    }

}
