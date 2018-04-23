package lzlzgame.service;

import com.github.pagehelper.PageHelper;
import lzlzgame.dao.entity.security.User;
import lzlzgame.dao.mapper.security.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageHelperTestService {
    @Autowired
    private UserMapper userMapper;

    public List<User> pagingUser(int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        return userMapper.selectAll();
    }
}
