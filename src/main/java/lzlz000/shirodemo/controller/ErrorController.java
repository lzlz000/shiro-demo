package lzlz000.shirodemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ErrorController {
    private final Map<String,String> errorMessageMap ;

    public ErrorController(){
        errorMessageMap = new HashMap<>();
        errorMessageMap.put("400","错误的请求");
        errorMessageMap.put("403","服务器拒绝请求");
        errorMessageMap.put("404","您正在寻找的页面不存");
        errorMessageMap.put("500"," 服务器遇到错误，无法完成请求");
        errorMessageMap.put("kickout","您的账号在其他设备登录，若非本人请修改密码");
    }


    @GetMapping("/err/{code}")
    public ModelAndView errRequest(@PathVariable("code") String code) {
        String message = errorMessageMap.get(code);
        if (message != null) {
            return new ModelAndView("error")
                    .addObject("code", code)
                    .addObject("message",message);
        }else{
            return new ModelAndView("error")
                    .addObject("code", "404")
                    .addObject("message",errorMessageMap.get("404"));
        }

    }
    //不显示错误代码只显示错误信息
    @GetMapping("/err/kickout")
    public ModelAndView exceptionRequest() {
        String message = errorMessageMap.get("kickout");
        if (message != null) {
            return new ModelAndView("error")
                    .addObject("message",message);
        }else{
            return new ModelAndView("error")
                    .addObject("code", "404")
                    .addObject("message",errorMessageMap.get("404"));
        }

    }
    @PostMapping("/err/{code}")
    @ResponseBody
    public Map<String,Object> postRequest(@PathVariable("code") String code) {
        String message = errorMessageMap.get(code);
        Map<String, Object> resultMap = new HashMap<>();
        if (message != null) {
            resultMap.put("code",code);
            resultMap.put("message",message);
        }else{
            resultMap.put("code",404);
            resultMap.put("message",errorMessageMap.get("404"));
        }
        return resultMap;
    }
}
