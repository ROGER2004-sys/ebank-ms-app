package org.example.ebankbot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;


@Service
public class ebankIAAgent {
    private final ChatClient chatClient;

    public ebankIAAgent(ChatClient.Builder builder, ChatMemory chatMemory
                             , ToolCallbackProvider tools) {
        this.chatClient = builder
                .defaultSystem("Tu es un assistant IA pour une banque en ligne. " +
                        "Tu dois répondre aux questions des clients de manière polie et professionnelle. " +
                        "Si tu ne connais pas la réponse," +
                        " dis simplement que tu ne sais pas et propose d'aider avec autre chose.")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultToolCallbacks(tools.getToolCallbacks())
                .build();
    }


    public String chat(String query, String conversationId) {
        return chatClient.prompt()
                .user(query)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}
