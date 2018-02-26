package lzlzgame.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 即时通讯用户
 */
@Data
@NoArgsConstructor
public class IMUser {
    String id;
    String name;
    long saveTime;
    /**
     * 过期时间，<=0即不过期
     */
    int expire;

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
