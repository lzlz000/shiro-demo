package lzlzgame;

import lzlzgame.entity.MyProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationBoot.class)
public class PropertiesTest {

    @Autowired
    MyProperties properties;

    @Test
    public void testValue(){
        System.out.println(properties.getListProp2());
    }

}
