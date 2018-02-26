package lzlzgame.service.im;

import lzlzgame.entity.Channel;
import lzlzgame.entity.CommonMessage;
import lzlzgame.entity.IMUser;
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
    IMUserService imUserService;

    private final Map<String,DeferredResult<CommonMessage>> resultMap = new ConcurrentHashMap<>();
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
        channelMap.values().forEach(channel -> channel.getSubscriptionSet().remove(user));
    }

    @Override
    public void emit(String channelName, IMUser sender , CommonMessage data) {
        sender.setSaveTime(new Date().getTime() + imUserService.getExpire());//发送消息时候更新savetime
        Channel channel = channelMap.get(channelName);
        if (channel!=null) {
            //当已达到过期时间 删除此user
            channel.getSubscriptionSet().removeIf(imUserService::isExpired);
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
    public DeferredResult<CommonMessage> poll(IMUser receiver){
        DeferredResult<CommonMessage> result = new DeferredResult<>(10000L);
        resultMap.put(receiver.getId(),result);
        result.onTimeout(()->{
            CommonMessage msg = new CommonMessage();
            msg.setErrmessage("time out");
            result.setResult(msg);
            resultMap.remove(receiver.getId());
        });
        return result;
    }

    private void send(IMUser receiver, CommonMessage message){
        if (receiver==null) {
            return;
        }
        DeferredResult<CommonMessage> result = resultMap.get(receiver.getId());
        // 偶尔会出现获取的result已经过期的情况,休眠300ms重新获取,重复2次仍没有数据则认为不存在接受者，
        // 此时仍有小概率出现数据丢失的情况，不过网络正常的情况下可能性不大了
        // 此处不可synchronized,因为前端会发送请求put进入resultMap
        for(int times = 4;(result == null||result.hasResult())&&times>0;times-- ){
            try {
                Thread.sleep(300);
                result = resultMap.get(receiver.getId());
            } catch (InterruptedException ignored) {
            }
        }
        if (result != null) {
            result.setResult(message);
            resultMap.remove(receiver.getId());
        }
    }
}
