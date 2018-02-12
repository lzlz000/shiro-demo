package lzlzgame.controller;

import lzlzgame.entity.CommonMessage;
import lzlzgame.entity.SendMessage;
import lzlzgame.entity.User;
import lzlzgame.service.IM.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * 试验长轮询的功能
 * createBy lzlzgame at 2018/2/10 15:35
 * @author : lzlzgame
 */
@RestController
@RequestMapping("polling")
public class LongPollingTestController {

    @Autowired
    ChannelService channelService;

    //长轮询
    @PostMapping("chat")
    @ResponseBody
    public DeferredResult<CommonMessage> chat(HttpServletRequest req){
        return channelService.poll(getUser(req.getSession()));
    }

    //订阅
    @PostMapping("subscribe")
    @ResponseBody
    public int subscribe(HttpServletRequest req,CommonMessage message){
        channelService.subscribe(message.getText(),getUser(req.getSession()));//测试时User的作用仅仅是作为map的key所以new一个即可
        return 0;
    }

    //@RequestBody需要接的参数是一个string化的json，直接传递json不需要此注解
    @PostMapping("send")
    public int send(SendMessage msg){
        channelService.emit(msg.getChannel(),msg.getSender(),msg.getMessage());
        return 0;
    }

    private synchronized User getUser(HttpSession session){
        User user =(User)session.getAttribute("user");
        if (user == null) {
            user = new User();//测试时User的作用仅仅是作为map的key所以new一个即可
            session.setAttribute("user",user);
        }
        return user;
    }
}
