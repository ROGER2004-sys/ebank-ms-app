package org.example.ebankbot.controllers;

import org.example.ebankbot.agents.ebankIAAgent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ebankbotController{
    private final ebankIAAgent ebankIAAgent;

    public ebankbotController(ebankIAAgent ebankIAAgent) {
        this.ebankIAAgent = ebankIAAgent;
    }

    @GetMapping(path = "/chat", produces = MediaType. TEXT_PLAIN_VALUE)
    public String chat(
            @RequestParam(name = "query",
                    defaultValue = "Hello, how can I help you today?") String query,
            @RequestParam(name = "conversationId",
                    defaultValue = "default") String conversationId) {
        return ebankIAAgent.chat(query, conversationId);
    }
}