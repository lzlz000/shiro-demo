package lzlzgame.entity;

import lzlzgame.dao.entity.security.User;

import java.util.Collection;

/**
 *
 */
public class SecurityUser {
    private final User user;

    private Collection<String> roles;
    private Collection<String> permissions;

    public SecurityUser(User user){
        this.user = user;
    }

    public int getId(){
        return user.getId();
    }
    public String getPassword(){
        return user.getPassword();
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    public Collection<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<String> permissions) {
        this.permissions = permissions;
    }
}
