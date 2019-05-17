package lzlz000.shirodemo.dao.mapper.security;

import lzlz000.shirodemo.dao.entity.security.Permission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface PermissionMapper extends Mapper<Permission> {

    @Select("SELECT p.* " +
            "LEFT JOIN permission_role pr ON r.id=pr.roleId " +
            "LEFT JOIN permission p ON pr.permissionId = p.id WHERE r.id=#{id}")
    List<Permission> selectPermissionByRoleId(@Param("id") int roleId);
}