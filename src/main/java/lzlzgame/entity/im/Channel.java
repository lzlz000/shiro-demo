package lzlzgame.entity.im;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lzlzgame.entity.im.IMUser;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    String name;
    Set<IMUser> subscriptionSet;
}
