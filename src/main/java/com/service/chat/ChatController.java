package com.service.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController
{

    @MessageMapping("chat.sendMessage")
    @SendTo("/topic/999")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        System.out.println("Send message: "+chatMessage);
        return chatMessage;
    }


    @MessageMapping("chat.addUser")
    @SendTo("/topic/999")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        System.out.println("Add User: "+chatMessage);
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}
