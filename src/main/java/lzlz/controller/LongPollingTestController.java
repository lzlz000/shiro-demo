package lzlz.controller;

import lzlz.entity.CommonMessage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 试验长轮询的功能
 * createBy lzlz at 2018/2/10 15:35
 * @author : lzlz
 */
@RestController
@RequestMapping("polling")
public class LongPollingTestController {
    private final List<DeferredResult<CommonMessage>> responseList = new CopyOnWriteArrayList<>();

    @PostMapping("chat")
    @ResponseBody
    public DeferredResult<CommonMessage> chat(){
        DeferredResult<CommonMessage> result =new DeferredResult<>(10000L);//10秒
        result.onTimeout(()->{
            CommonMessage msg = new CommonMessage();
            msg.setErrmessage("time out!");
            result.setResult(msg);
            responseList.remove(result);//从list中删除此内容
        });//超时任务
        responseList.add(result);// 把请求响应的DeferredResult实体放到第一个响应List中
        return result;
    }
    //@RequestBody需要接的参数是一个string化的json，直接传递json不需要此注解
    @PostMapping("send")
    public int send(CommonMessage msg){
        responseList.forEach(value->value.setResult(msg));
        return 0;
    }
}
