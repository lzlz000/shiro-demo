package lzlzgame.entity;

import lombok.Data;

@Data
public class SendMessage {
    String channel;
    User sender;
    CommonMessage message;
}
