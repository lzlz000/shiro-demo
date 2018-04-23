package lzlzgame.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import lzlzgame.service.LoginService;
import org.apache.shiro.authc.AccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
@Controller
public class LoginController {
    private static final String VERIFY_KEY=Constants.KAPTCHA_SESSION_KEY+"_LOGIN";
    @Autowired
    private DefaultKaptcha captchaProducer;
    @Autowired
    private LoginService loginService;

    /**
     * 登录界面
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * post提交登录信息
     * @param username 用户名
     * @param password 密码
     * @param verify 验证码
     */
    @PostMapping("/login")
    @ResponseBody
    public Map<String,Object> submitLogin(String username, String password, String verify,
                                          HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        try {
            String verifyText = (String)request.getSession().getAttribute(VERIFY_KEY);
            //如果验证码正确
            if(verifyText!=null&&verifyText.equals(verify)){
                request.getSession().setAttribute(VERIFY_KEY, null);
                loginService.login(username, password);
                resultMap.put("status", 200);
                resultMap.put("message", "登录成功");
            }else{
                throw new AccountException("验证码错误");
            }

        } catch (Exception e) {
            resultMap.put("status", 500);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    //获取登录验证码
    @GetMapping("/login/verify")
    public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setDateHeader("Expires", 0);

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/gif");
        // create the text for the image
        String capText = captchaProducer.createText();
        // store the text in the session
        //request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        //将验证码存到session
        request.getSession().setAttribute(VERIFY_KEY, capText);
        log.info(capText);
        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "gif", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }
}
