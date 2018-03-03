package lzlzgame.service.im;

import lzlzgame.entity.CommonMessage;
import lzlzgame.entity.im.IMUser;
import org.springframework.web.context.request.async.DeferredResult;

public interface IChannelService {
    /**
     * 订阅频道
     * @param channelName 频道名
     * @param user 用户
     */
    void subscribe(String channelName, IMUser user);
    /**
     * 取消订阅频道
     * @param channelName 频道名
     * @param user 用户
     */
    void unsubscribe(String channelName, IMUser user);
    /**
     * 取消订阅所有频道
     * @param user 用户
     */
    void unsubscribe(IMUser user);
    /**
     * 发送
     * @param channelName 频道名
     * @param sender 发送者
     */
    void emit(String channelName, IMUser sender, CommonMessage Data);

    /**
     * 长轮询
     * @param receiver 接受者
     */
    DeferredResult poll(IMUser receiver);
}
