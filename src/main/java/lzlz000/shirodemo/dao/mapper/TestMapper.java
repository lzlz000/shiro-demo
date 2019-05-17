package lzlz000.shirodemo.dao.mapper;

import lzlz000.shirodemo.dao.entity.TestDO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TestMapper extends Mapper<TestDO> {
    @Select("select * from test where id<=#{id}")
    List<TestDO> selectTest(int id);
}
