package lzlzgame.entity;

import lzlzgame.dao.entity.security.User;

import java.util.Set;


/**
 *
 */
public class MySecurityUser {
    private final User user;

    private Set<String> roles;
    private Set<String> permissions;

    public MySecurityUser(User user){
        this.user = user;
    }

    public int getId(){
        return user.getId();
    }
    public String getPassword(){
        return user.getPassword();
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
