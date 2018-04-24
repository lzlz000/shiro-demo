package lzlzgame.entity.im;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 即时通讯客户端
 */
@Data
@NoArgsConstructor
public class IMClient {
    String id;
    String name;
    long saveTime;
    /**
     * 过期时间 ms，<=0即不过期
     */
    int expire;

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
