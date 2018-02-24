package lzlzgame.service.IM;

import lzlzgame.entity.Channel;
import lzlzgame.entity.CommonMessage;
import lzlzgame.entity.User;
import lzlzgame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用ConcurrentHashMap实现的频道订阅
 * DeferredResult 长轮询实现即时通讯
 * createBy lzlz at 2018/2/12 10:56
 * @author : lzlz
 */
@Service
public class ChannelService implements IChannelService{
    @Autowired
    UserService userService;

    private final Map<User,DeferredResult<CommonMessage>> resultMap = new ConcurrentHashMap<>();
    //不应向外部提供任何Channel的引用
    private final Map<String,Channel> channelMap = new ConcurrentHashMap<>();

    @Override
    public void subscribe(String channelName, User user) {
        Channel channel = channelMap.get(channelName);
        if (channel==null) {
            channel = new Channel(channelName,new HashSet<>());
            channelMap.put(channelName,channel);
        }
        channel.getSubscriptionSet().add(user);
    }

    @Override
    public void unsubscribe(String channelName, User user) {
        Channel channel = channelMap.get(channelName);
        if (channel==null) {
            return;
        }
        Set subscriptionSet = channel.getSubscriptionSet();
        subscriptionSet.remove(user);
        if(subscriptionSet.size()==0){
            channelMap.remove(channelName);
        }
    }

    @Override
    public void unsubscribe(User user) {
        channelMap.values().forEach(channel -> channel.getSubscriptionSet().remove(user));
    }

    @Override
    public void emit(String channelName, User sender ,CommonMessage data) {
        sender.setSaveTime(new Date().getTime() + userService.getExpire());//发送消息时候更新savetime
        Channel channel = channelMap.get(channelName);
        if (channel!=null) {
            //当已达到过期时间 删除此user
            channel.getSubscriptionSet().removeIf(user ->user.getSaveTime()<= new Date().getTime());
            channel.getSubscriptionSet().forEach(user-> send(user,data));
            //当channel中没有订阅者，删除此channel
            if( channel.getSubscriptionSet().size()==0){
                channelMap.remove(channelName);
            }
        }

    }

    /**
     * 长轮询
     */
    @Override
    public DeferredResult<CommonMessage> poll(User receiver){
        DeferredResult<CommonMessage> result = new DeferredResult<>(10000L);
        resultMap.put(receiver,result);
        result.onTimeout(()->{
            CommonMessage msg = new CommonMessage();
            msg.setErrmessage("time out");
            result.setResult(msg);
            resultMap.remove(receiver);//从list中删除此内容
        });
        return result;
    }

    private void send(User receiver, CommonMessage message){
        DeferredResult<CommonMessage> result = resultMap.get(receiver);
        //偶然会出现获取的result已经过期的情况 重新获取result;
        while(result != null&&result.hasResult()){
            result = resultMap.get(receiver);
        }
        if (result != null) {
            result.setResult(message);
        }
    }
}
