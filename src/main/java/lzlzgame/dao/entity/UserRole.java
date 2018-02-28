package lzlzgame.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "user_role")
@Data
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "userId")
    int userId;

    @Column(name = "roleId")
    int roleId;


}
