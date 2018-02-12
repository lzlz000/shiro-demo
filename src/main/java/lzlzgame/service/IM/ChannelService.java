package lzlzgame.service.IM;

import lzlzgame.entity.Channel;
import lzlzgame.entity.CommonMessage;
import lzlzgame.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用map实现的最简易频道订阅
 * createBy lzlz at 2018/2/12 10:56
 * @author : lzlz
 */
@Service
public class ChannelService implements IChannelService{

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
    public void unsubscribeAll(User user) {
        channelMap.values().forEach(channel -> channel.getSubscriptionSet().remove(user));
    }

    @Override
    public void emit(String channelName, User sender ,CommonMessage data) {
        Channel channel = channelMap.get(channelName);
        if (channel!=null) {
            channel.getSubscriptionSet().forEach(user-> send(user,data));
        }

    }

    /**
     * 长轮询
     */
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
        if (result != null&&!result.hasResult()) {
            result.setResult(message);
        }
    }
}
