package lzlzgame.controller;

import lzlzgame.entity.CommonMessage;
import lzlzgame.entity.SendMessage;
import lzlzgame.service.IM.ChannelService;
import lzlzgame.service.UserService;
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
    private UserService userService;

    //长轮询
    @PostMapping("chat")
    @ResponseBody
    public DeferredResult<CommonMessage> chat(HttpServletRequest req){
        return channelService.poll(userService.getSessionUser(req.getSession()));
    }

    //订阅
    @PostMapping("subscribe")
    @ResponseBody
    public String subscribe(HttpServletRequest req,CommonMessage message){
        channelService.subscribe(message.getText(),userService.getSessionUser(req.getSession()));//测试时User的作用仅仅是作为map的key所以new一个即可
        return "subscribe success";
    }
    //取消订阅
    @PostMapping("unsubscribe")
    @ResponseBody
    public String unsubscribe(HttpServletRequest req,CommonMessage message){
        channelService.unsubscribe(message.getText(),userService.getSessionUser(req.getSession()));//测试时User的作用仅仅是作为map的key所以new一个即可
        return "unsubscribe success";
    }

    /*@RequestBody需要接的参数是一个string化的json,前端需要JSON.stringify不这么搞会报错:
    org.springframework.beans.InvalidPropertyException: Invalid property 'message[text]'
    of bean class [lzlzgame.entity.SendMessage]: Property referenced in indexed property path
    'message[text]' is neither an array nor a List nor a Map;
     */
    @PostMapping("send")
    public int send(HttpServletRequest req, @RequestBody SendMessage msg){
        msg.setSender(userService.getSessionUser(req.getSession()));
        channelService.emit(msg.getChannel(),msg.getSender(),msg.getMessage());
        return 0;
    }

}
