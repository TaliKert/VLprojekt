package com.kmk.imageboard.controller.api;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/streamsubscribe")
    @SendTo("/topic/streamupdate")
    public void newImagePush() { }
}
