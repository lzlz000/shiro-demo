package lzlzgame.service.IM;

import lzlzgame.entity.CommonMessage;
import lzlzgame.entity.User;

public interface IChannelService {
    /**
     * 订阅频道
     * @param channelName 频道名
     * @param user 用户
     */
    void subscribe(String channelName, User user);
    /**
     * 取消订阅频道
     * @param channelName 频道名
     * @param user 用户
     */
    void unsubscribe(String channelName, User user);
    /**
     * 取消订阅所有频道
     * @param user 用户
     */
    void unsubscribeAll(User user);
    /**
     * 发送
     * @param channelName 频道名
     * @param sender 发送者
     */
    void emit(String channelName, User sender,CommonMessage Data);
}
