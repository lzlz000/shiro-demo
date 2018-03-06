package lzlzgame.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试使用自定义properties
 */
@Data
public class MyProperties {
    /*
    simpleProp: simplePropValue
    arrayProps: 1,2,3,4,5
    listProp1:
      - name: abc
        value: abcValue
      - name: efg
        value: efgValue
    listProp2:
      - config2Value1
      - config2Vavlue2
    mapProps:
      key1: value1
      key2: value2
     */
    private String simpleProp;
    private int[] arrayProps;
    private List<Map<String, String>> listProp1 = new ArrayList<>(); //接收prop1里面的属性值
    private List<String> listProp2 = new ArrayList<>(); //接收prop2里面的属性值
    private Map<String, String> mapProps = new HashMap<>(); //接收prop1里面的属性值
}
