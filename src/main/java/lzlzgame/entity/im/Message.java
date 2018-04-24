package lzlzgame.entity.im;

import lombok.Data;
import lzlzgame.entity.CommonMessage;

@Data
public class Message {
    String channel;
    IMClient sender;
    CommonMessage message;
}
