package lzlzgame.dao.mapper;

import lzlzgame.dao.entity.CustomUserDetails;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserDetailsMapper extends Mapper<CustomUserDetails> {

}