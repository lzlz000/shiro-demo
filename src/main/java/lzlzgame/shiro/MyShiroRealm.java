package lzlzgame.shiro;

import lzlzgame.dao.entity.security.Role;
import lzlzgame.dao.entity.security.User;
import lzlzgame.dao.mapper.security.RoleMapper;
import lzlzgame.dao.mapper.security.UserMapper;
import lzlzgame.entity.SecurityUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    //授权 （角色）
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SecurityUser user =(SecurityUser)SecurityUtils.getSubject().getPrincipal();
        Collection<String> roles = user.getRoles();
        if (roles==null) {
            roles = roleMapper.selectRolesByUserId(user.getId()).stream()
                    .map(Role::getName).collect(Collectors.toSet());
            user.setRoles(roles);
        }
        SimpleAuthorizationInfo info =  new SimpleAuthorizationInfo();
        info.addRoles(roles);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        List<User> userList = userMapper.login(token.getUsername(),new String(token.getPassword()));
        SecurityUser user = null;
        if (userList.size()>0) {
            user =  new SecurityUser(userList.get(0));
        }
        if (user==null) {
            throw new AccountException("帐号或密码不正确！");
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }
}
