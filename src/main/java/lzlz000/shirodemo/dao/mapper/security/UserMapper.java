package lzlz000.shirodemo.dao.mapper.security;

import lzlz000.shirodemo.dao.entity.security.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface UserMapper extends Mapper<User> {
    @Select("SELECT `id`,`username`,`password` FROM `user` " +
            "WHERE `username`=#{username} AND `password`=#{password}")
    List<User> login(@Param("username")String username, @Param("password")String password);
}