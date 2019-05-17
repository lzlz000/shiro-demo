package lzlz000.shirodemo.entity.im;

import lombok.Data;
import lzlz000.shirodemo.entity.CommonMessage;

@Data
public class Message {
    String channel;
    IMClient sender;
    CommonMessage message;
}
