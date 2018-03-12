package lzlzgame.filter;

import lzlzgame.service.LoginService;
import lzlzgame.utils.ApplicationContextProvider;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 踢出被标注kickout的session
 */
public class KickoutFilter extends AccessControlFilter {
    private LoginService loginService;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }
        //filter中无法注入bean只能手动注入了 不知道有没有好办法
        if (loginService==null) {
            loginService = ApplicationContextProvider.getBean(LoginService.class);
        }
        if (loginService.kickout()) {
            servletRequest.getRequestDispatcher("err/kickout").forward(servletRequest,servletResponse);
            return false;
        }
        return true;
    }
}
