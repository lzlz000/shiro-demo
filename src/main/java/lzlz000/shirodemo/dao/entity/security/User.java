package lzlz000.shirodemo.dao.entity.security;

import lombok.Data;

import javax.persistence.*;

@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;
}
