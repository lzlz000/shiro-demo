package lzlzgame.service.im;

import lzlzgame.entity.Channel;
import lzlzgame.entity.CommonMessage;
import lzlzgame.entity.IMUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;
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
    IMUserService imUserService;

    private final Map<String,ResultSender> resultMap = new ConcurrentHashMap<>();
    //不应向外部提供任何Channel的引用
    private final Map<String,Channel> channelMap = new ConcurrentHashMap<>();

    @Override
    public void subscribe(String channelName, IMUser user) {
        Channel channel = channelMap.get(channelName);
        if (channel==null) {
            channel = new Channel(channelName,new HashSet<>());
            channelMap.put(channelName,channel);
        }
        channel.getSubscriptionSet().add(user);
    }

    @Override
    public void unsubscribe(String channelName, IMUser user) {
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
    public void unsubscribe(IMUser user) {
        if (user != null) {
            channelMap.values().forEach(channel -> channel.getSubscriptionSet().remove(user));
        }
    }

    @Override
    public void emit(String channelName, IMUser sender , CommonMessage data) {
        sender.setSaveTime(new Date().getTime() + imUserService.getExpire());//发送消息时候更新savetime
        Channel channel = channelMap.get(channelName);
        if (channel!=null) {
            //当已达到过期时间 删除此user,并且从resultMap中删除对应的ResultSender
            channel.getSubscriptionSet().removeIf(imUser -> {
                boolean isExpired = imUserService.isExpired(imUser);
                if(isExpired){
                    resultMap.remove(imUser.getId());
                }
                return isExpired;
            });
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
    public DeferredResult<List<CommonMessage>> poll(IMUser receiver){
        ResultSender sender = resultMap.get(receiver.getId());
        if (sender==null) {
            sender = new ResultSender();
            resultMap.put(receiver.getId(),sender);
        }
        return sender.poll();
    }


    private void send(IMUser receiver, CommonMessage message){
        ResultSender sender = resultMap.get(receiver.getId());
        if (sender != null) {
            sender.send(message);
        }
    }
}
