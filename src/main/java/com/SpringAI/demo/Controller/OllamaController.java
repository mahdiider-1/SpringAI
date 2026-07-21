package com.SpringAI.demo.Controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> getAnswer(@PathVariable String message){

        try {
            ChatResponse response = chatClient
                    .prompt(message)
                    .advisors(a -> a.param(
                            ChatMemory.CONVERSATION_ID,
                            "user-1"
                    ))
                    .call()
                    .chatResponse();

            System.out.println(response.getMetadata().getModel());
            System.out.println(response.getMetadata().getUsage().getCompletionTokens());

            String chatResponse = response.getResult().getOutput().getText();

            return ResponseEntity.ok(chatResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unable to process your request");
        }

    }

}
