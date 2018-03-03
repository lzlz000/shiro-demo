package lzlzgame.entity;

import lzlzgame.dao.entity.security.User;

import java.util.Collection;

public class MySecurityUser {
    private final User user;

    private Collection<String> roles;

    public MySecurityUser(User user){
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
}
