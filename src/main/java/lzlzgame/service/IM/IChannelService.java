package lzlzgame.service.IM;

import lzlzgame.entity.CommonMessage;
import lzlzgame.entity.User;
import org.springframework.web.context.request.async.DeferredResult;

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
    void unsubscribe(User user);
    /**
     * 发送
     * @param channelName 频道名
     * @param sender 发送者
     */
    void emit(String channelName, User sender,CommonMessage Data);

    /**
     * 长轮询
     * @param receiver 接受者
     */
    DeferredResult poll(User receiver);
}
