package lzlzgame.filter;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 踢出被标注kickout的session
 */
public class SingleLoginSessionFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
        return false;
    }


    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest,servletResponse);
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }
        //如果需要踢出用户
        if( subject.getSession().getAttribute("kickout") != null){
            subject.logout();
            subject.getSession().setAttribute("kickout",null);
            servletRequest.getRequestDispatcher("error/kickout").forward(servletRequest,servletResponse);
            return false;
        }

        return true;
    }
}
