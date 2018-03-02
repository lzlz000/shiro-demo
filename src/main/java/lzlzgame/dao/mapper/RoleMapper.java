package lzlzgame.dao.mapper;

import lzlzgame.dao.entity.Role;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RoleMapper extends Mapper<Role> {

    @Select("SELECT r.id,r.name FROM user u " +
            "LEFT JOIN user_role ur ON u.id=ur.userId " +
            "LEFT JOIN role r ON ur.roleId = r.id WHERE u.id=#{id}")
    List<Role> selectRolesByUserId(int id);
}