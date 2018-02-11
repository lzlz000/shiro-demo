package lzlzgame;

import lzlz.entity.CommonMessage;
import org.junit.Test;

public class Test1 {
    @Test
    public void test1(){
        CommonMessage messge = new CommonMessage();
        messge.setMessage("aaa");
        System.out.println(messge.getMessage());
    }
}
