package lzlz.controller;

import lzlz.entity.CommonMessage;
import lzlz.entity.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 试验长轮询的功能
 * createBy lzlz at 2018/2/10 15:35
 * @author : lzlz
 */
@RestController
@RequestMapping("polling")
public class LongPollingTestController {
    final Map<String,CommonMessage> requestBodyMap  = new ConcurrentHashMap<>();
    final Map<String,DeferredResult<CommonMessage>> responseBodyMap  = new ConcurrentHashMap<>();

    @PostMapping("chat")
    @ResponseBody
    public DeferredResult<CommonMessage> chat(@RequestBody CommonMessage req){
        DeferredResult<CommonMessage> result =new DeferredResult<>(10000l);//10秒
        result.onTimeout(()->{
            CommonMessage resp = new CommonMessage();
            resp.setErrmessage("time out!");
            result.setResult(resp);
        });//超时任务
        requestBodyMap.put("1", req);// 把请求放到第一个请求map中
        responseBodyMap.put("1", result);// 把请求响应的DeferredResult实体放到第一个响应map中
        return result;
    }
//    @PostMapping("send")
//    @ResponseBody
//    public DeferredResult<CommonMessage> send(@RequestBody CommonMessage msg){
//        requestBodyMap.put("1", msg);// 把请求放到第一个请求map中
//        responseBodyMap.put("1", result);// 把请求响应的DeferredResult实体放到第一个响应map中
//        return result;
//    }
}
