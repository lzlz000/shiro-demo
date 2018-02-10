package lzlz.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonMessage {
    String message;
    String errmessage;
    Object data;
}
