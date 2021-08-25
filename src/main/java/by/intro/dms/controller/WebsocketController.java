package by.intro.dms.controller;

import by.intro.dms.model.WebsocketInputMessage;
import by.intro.dms.model.WebsocketOutputMessage;
import by.intro.dms.service.UserAccountService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {

    private final UserAccountService userAccountService;

    public WebsocketController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public WebsocketOutputMessage sendMessage(WebsocketInputMessage message) throws InterruptedException {
        String firstName = userAccountService.getAuthorizedUser().getFirstName();
        Thread.sleep(500);
        return new WebsocketOutputMessage("" + message.getInputMessage(), firstName);
    }
}
