package lzlzgame.dao.entity.security;

import lombok.Data;

import javax.persistence.*;

@Table(name = "permission")
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //权限名称
    @Column(name = "name")
    private String name;

    //权限描述
    @Column(name = "descritpion")
    private String descritpion;

    //授权链接
    @Column(name = "url")
    private String url;
}