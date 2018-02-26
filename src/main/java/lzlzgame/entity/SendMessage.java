package lzlzgame.entity;

import lombok.Data;

@Data
public class SendMessage {
    String channel;
    IMUser sender;
    CommonMessage message;
}
