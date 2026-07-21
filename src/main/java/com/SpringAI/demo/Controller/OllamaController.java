package com.SpringAI.demo.Controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("api/movie")
    public ResponseEntity<String> movieRec(@RequestParam String genre,@RequestParam String year,@RequestParam String language){

        try {
            String template = "Recommend movies based on the following criteria:" +
                    "" +
                    "Genre: {genre}" +
                    "Release Year: {year}" +
                    "Language: {language}" +
                    "" +
                    "Requirements:" +
                    "- Recommend 10–20 movies." +
                    "- Include only movies that match all the criteria." +
                    "- Sort them by IMDb rating (highest first)." +
                    "- For each movie provide:" +
                    "  - Title" +
                    "  - Release Year" +
                    "  - IMDb Rating" +
                    "  - Genre" +
                    "  - Language" +
                    "  - Runtime" +
                    "  - A brief spoiler-free synopsis (2–3 sentences)" +
                    "  - Why it is worth watching" +
                    "" +
                    "Do not recommend TV series." +
                    "Avoid duplicate or unrelated suggestions.";

            PromptTemplate promptTemplate = PromptTemplate.builder()
                    .template(template)
                    .variables(Map.of("genre", genre, "year", year, "language", language))
                    .build();

            Prompt prompt = promptTemplate.create();

            ChatResponse chatResponse = chatClient
                    .prompt(prompt)
                    .system("Act as an expert movie recommendation system.")
                    .advisors(a -> a.param(
                            ChatMemory.CONVERSATION_ID,
                            "user-1"
                    ))
                    .call()
                    .chatResponse();

            String response = chatResponse.getResult().getOutput().getText();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unable to process your request");
        }

    }

}
