package com.starter.SpringAI;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OpenAIController {

    private ChatClient chatClients;

    @Autowired
    @Qualifier("openAiEmbeddingModel")
    private EmbeddingModel embeddingModel;

    public OpenAIController(OpenAiChatModel chatModel){
        this.chatClients = ChatClient.create(chatModel);
    }

 //   ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

 //   public OpenAIController (ChatClient.Builder builder) {
 //       this.chatClients = builder
 //               .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
 //               .build();
 //   }


    @PostMapping("/api/recommend")
    public String recommend(@RequestParam String type,
                            @RequestParam String year,
                            @RequestParam String lang) {

        String temp = """
            I want to watch a {type} movie tonight with good rating, 
            looking for movies around this year {year}. 
            The language I'm looking for is {lang}.
            Suggest one specific movie and tell me the cast and length of the movie.
            
            response format should be:
            1. Movie Name
            2. basic plot
            3. cast
            4. length
            5. IMDB rating
            """;

        PromptTemplate promptTemplate = new PromptTemplate(temp);

        Prompt prompt = promptTemplate.create(Map.of(
                "type", type,
                "year", year,
                "lang", lang
        ));

        String response = chatClients
                .prompt(prompt)
                .call()
                .content();

        return response;
    }

    @PostMapping("/api/embedding")
    public float[] embeddings(@RequestParam String text){
        return embeddingModel.embed(text);
    }

}
