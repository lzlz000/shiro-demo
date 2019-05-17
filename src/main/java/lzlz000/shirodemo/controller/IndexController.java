package lzlz000.shirodemo.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController{


    @GetMapping("/")
    public ModelAndView main() {
        return new ModelAndView("index")
                .addObject("hello", "主页");
    }
    @GetMapping("/index")
    public String index() {
        Subject subject = SecurityUtils.getSubject();
        //如果已经登录
        if (subject.isAuthenticated() || subject.isRemembered()) {
            if (subject.hasRole("ADMIN"))
                return "redirect:/admin";
            return "redirect:/user";

        }
        return "redirect:/";
    }
    @GetMapping({"/admin"})
    public String admin() {
        return "admin";
    }
    @GetMapping({"/user"})
    public String user() {
        return "user";
    }

}
