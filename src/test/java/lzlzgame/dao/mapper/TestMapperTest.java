package lzlzgame.dao.mapper;

import lzlzgame.ApplicationBoot;
import lzlzgame.service.PageHelperTestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationBoot.class)
public class TestMapperTest {
    @Autowired
    TestMapper testMapper;
    @Autowired
    PageHelperTestService pageHelperTestService;

    @Test
    public void testShow() {
        pageHelperTestService.pagingUser(1,2).forEach(System.out::println);
        pageHelperTestService.pagingUser(2,2).forEach(System.out::println);
    }

}
