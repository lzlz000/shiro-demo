package lzlz000.shirodemo.shiro;

import lzlz000.shirodemo.entity.MySecurityUser;
import lzlz000.shirodemo.dao.entity.security.Role;
import lzlz000.shirodemo.dao.entity.security.User;
import lzlz000.shirodemo.dao.mapper.security.RoleMapper;
import lzlz000.shirodemo.dao.mapper.security.UserMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    //授权 （角色）
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        MySecurityUser user =(MySecurityUser)SecurityUtils.getSubject().getPrincipal();
        Set<String> roles = user.getRoles();
        if (roles==null) {
            roles = roleMapper.selectRolesByUserId(user.getId()).stream()
                    .map(Role::getName).collect(Collectors.toSet());
            user.setRoles(roles);
        }
        SimpleAuthorizationInfo info =  new SimpleAuthorizationInfo();
        info.setRoles(roles);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        List<User> userList = userMapper.login(token.getUsername(),new String(token.getPassword()));
        MySecurityUser user = null;
        if (userList.size()>0) {
            user =  new MySecurityUser(userList.get(0));
        }
        if (user==null) {
            throw new AccountException("帐号或密码不正确！");
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }
}
