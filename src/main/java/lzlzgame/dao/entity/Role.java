package lzlzgame.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "name")
    String name;
}
