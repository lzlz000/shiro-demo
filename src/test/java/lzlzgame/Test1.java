package lzlzgame;

import lzlzgame.entity.CommonMessage;
import org.junit.Test;

public class Test1 {
    @Test
    public void test1(){
        CommonMessage messge = new CommonMessage();
        messge.setText("aaa");
        System.out.println(messge.getText());
    }
}
