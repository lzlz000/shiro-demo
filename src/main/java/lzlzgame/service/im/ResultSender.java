package lzlzgame.service.im;


import lzlzgame.entity.CommonMessage;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 长轮询消息发送类
 * 长轮询过程中，消息通常可以即时发送，然而在消息发送给前端，前端发送下一个请求到服务器之间的这段时间，
 * 消息是发送不出去的，特别是有多个消息在短时间内发送，会丢失，此类解决这个问题
 *
 * flush()方法会在DeferredResult可用时把消息发送出去，在send和poll时都会执行flush(),
 * 这样无论什么情况下消息最终都会被发送出去
 * createBy lzlz at 2018/2/27 13:21
 * @author : lzlz
 */
public class ResultSender {
    private volatile DeferredResult<List<CommonMessage>> result;

    //使用linkedList 作为消息队列
    private final List<CommonMessage> messageQueue = new LinkedList<>();


    public synchronized void send(CommonMessage message){
        messageQueue.add(message);
        flush();
    }
    public DeferredResult<List<CommonMessage>> poll(){
        result = new DeferredResult<>(10000L);
        flush();
        result.onTimeout(()->result.setResult(null));
        return result;
    }

    private synchronized void flush(){
        if (result!=null&&!result.hasResult()&&messageQueue.size()>0) {
            //这里需要拷贝一份消息，否则最后发送出去的是clear之后的消息
            result.setResult(new ArrayList<>(messageQueue));
            messageQueue.clear();
        }
    }

}
