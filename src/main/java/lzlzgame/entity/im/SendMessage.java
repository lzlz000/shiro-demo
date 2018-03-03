package lzlzgame.entity.im;

import lombok.Data;
import lzlzgame.entity.CommonMessage;

@Data
public class SendMessage {
    String channel;
    IMUser sender;
    CommonMessage message;
}
