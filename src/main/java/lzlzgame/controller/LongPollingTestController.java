package lzlzgame.controller;

import lzlzgame.entity.CommonMessage;
import lzlzgame.entity.SendMessage;
import lzlzgame.service.im.ChannelService;
import lzlzgame.service.im.IMUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;


/**
 * 试验长轮询的功能
 * createBy lzlzgame at 2018/2/10 15:35
 * @author : lzlzgame
 */
@RestController
@RequestMapping("polling")
public class LongPollingTestController {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private IMUserService userService;

    //长轮询
    @PostMapping("chat")
    @ResponseBody
    public DeferredResult<CommonMessage> chat(HttpServletRequest req){
        return channelService.poll(userService.getIMUser(req.getSession()));
    }

    //订阅
    @PostMapping("subscribe")
    public String subscribe(HttpServletRequest req,SendMessage channel){
        channelService.subscribe(channel.getChannel(),userService.getIMUser(req.getSession()));//测试时User的作用仅仅是作为map的key所以new一个即可
        return "订阅成功";
    }
    //取消订阅
    @PostMapping("unsubscribe")
    public String unsubscribe(HttpServletRequest req,SendMessage channel){
        channelService.unsubscribe(channel.getChannel(),userService.getIMUser(req.getSession()));//测试时User的作用仅仅是作为map的key所以new一个即可
        return "取消订阅";
    }

    /*
    前端需要JSON.stringify，不这么搞会报错，似乎识别不了内部的自定义对象CommonMessage:
    org.springframework.beans.InvalidPropertyException: Invalid property 'message[text]'
    of bean class [lzlzgame.entity.SendMessage]: Property referenced in indexed property path
    'message[text]' is neither an array nor a List nor a Map;
     */
    @PostMapping("send")
    public String send(HttpServletRequest req, @RequestBody SendMessage msg){
        msg.setSender(userService.getIMUser(req.getSession()));
        channelService.emit(msg.getChannel(),msg.getSender(),msg.getMessage());
        return "发送成功";
    }

}
