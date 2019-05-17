package lzlz000.shirodemo.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "test")
@Data
public class TestDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String message;

}
