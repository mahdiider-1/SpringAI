package com.SpringAI.demo.Controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ollama")
public class OllamaController {

    @Autowired
    private ChatClient chatClient;


    @GetMapping("api/{message}")
    public String getAnswer(@PathVariable String message){

        return chatClient
                .prompt(message)
                .advisors(a-> a.param(
                        ChatMemory.CONVERSATION_ID,
                        "user-1"
                ))
                .call()
                .content();

    }

}
