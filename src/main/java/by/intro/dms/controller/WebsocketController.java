package by.intro.dms.controller;

import by.intro.dms.model.WebsocketOutputMessage;
import by.intro.dms.model.WebsocketUser;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class WebsocketController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public WebsocketOutputMessage sendMessage(WebsocketUser user) throws InterruptedException {
        Thread.sleep(500);
        return new WebsocketOutputMessage("Hello, " + HtmlUtils.htmlEscape(user.getName()) + "!");
    }
}
