package lzlzgame.service;

import lzlzgame.dao.entity.Role;
import lzlzgame.dao.entity.User;
import lzlzgame.dao.mapper.RoleMapper;
import lzlzgame.dao.mapper.UserMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        User user =(User)SecurityUtils.getSubject().getPrincipal();
        List<String> roles = roleMapper.selectRolesByUserId(user.getId()).stream()
                .map(Role::getName).collect(Collectors.toList());
        SimpleAuthorizationInfo info =  new SimpleAuthorizationInfo();
        info.addRoles(roles);
        return info;
    }

    //认证 （登录）
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        List<User> userList = userMapper.login(token.getUsername(),new String(token.getPassword()));
        User user = null;
        if (userList.size()>0) {
            user =  userList.get(0);
        }
        if (user==null) {
            throw new AccountException("帐号或密码不正确！");
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }
}
