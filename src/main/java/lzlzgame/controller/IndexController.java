package lzlzgame.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class IndexController{

    @GetMapping("/index")
    public ModelAndView index() {

        return new ModelAndView("index")
                .addObject("hello", "hello world!");
    }
    @GetMapping({"/","/login"})
    public ModelAndView login() {
        return new ModelAndView("login");
    }
    @GetMapping({"/admin"})
    public ModelAndView admin() {
        return new ModelAndView("admin");
    }

    @PostMapping(value="/login")
    @ResponseBody
    public Map<String,Object> submitLogin(String username, String password) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            SecurityUtils.getSubject().login(token);
            resultMap.put("status", 200);
            resultMap.put("message", "登录成功");

        } catch (Exception e) {
            resultMap.put("status", 500);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
    @RequestMapping("logout")
    public String submitLogout() {
        SecurityUtils.getSubject().logout();
        return "/login";
    }
}
