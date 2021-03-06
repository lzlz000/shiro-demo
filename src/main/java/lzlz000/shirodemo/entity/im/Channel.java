package lzlz000.shirodemo.entity.im;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    String name;
    Set<IMClient> subscriptionSet;
}
