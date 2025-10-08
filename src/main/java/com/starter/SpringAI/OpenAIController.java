package com.starter.SpringAI;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenAIController {

    private ChatClient chatClients;

    public OpenAIController(OpenAiChatModel chatModel){
        this.chatClients = ChatClient.create(chatModel);
    }


    @GetMapping("/api/{message}")
    public ResponseEntity<String> getAnswer(@PathVariable String message)
    {
        ChatResponse chatResponse = chatClients
                .prompt(message)
                .call()
                .chatResponse();

        System.out.println(chatResponse.getMetadata().getModel());
        System.out.println(chatResponse.getMetadata().getUsage().getTotalTokens());


        String response = chatResponse
                .getResult()
                .getOutput()
                .getText();

        return ResponseEntity.ok(response);
    }
}
