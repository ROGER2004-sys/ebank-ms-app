package org.example.ebankbot.agents;

import java.text.Normalizer;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Flux;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;
import org.springframework.stereotype.Service;


@Service
public class ebankIAAgent {
    private final ChatClient chatClient;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String customerServiceUrl;
    private final String accountServiceUrl;

    public ebankIAAgent(ChatClient.Builder builder, ChatMemory chatMemory
                             , ToolCallbackProvider tools, RestClient.Builder restClientBuilder,
                        ObjectMapper objectMapper,
                        @Value("${banking-services.customers-url}") String customerServiceUrl,
                        @Value("${banking-services.accounts-url}") String accountServiceUrl) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
        this.customerServiceUrl = customerServiceUrl;
        this.accountServiceUrl = accountServiceUrl;
        this.chatClient = builder
                .defaultSystem("Tu es un assistant IA pour une banque en ligne. " +
                        "Tu dois répondre aux questions des clients de manière polie et professionnelle. " +
                        "Pour toute information sur les clients ou les comptes, utilise les outils bancaires " +
                        "et ne fabrique jamais de données. Si tu ne connais pas la réponse," +
                        " dis simplement que tu ne sais pas et propose d'aider avec autre chose.")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultToolCallbacks(tools.getToolCallbacks())
                .build();
    }


    public String chat(String query) {
        return chat(query, "default");
    }

    public String chat(String query, String conversationId) {
        return buildPrompt(query, conversationId)
                .call()
                .content();
    }

    public Flux<String> chatStream(String query, String conversationId) {
        return buildPrompt(query, conversationId)
                .stream()
                .content();
    }

    private ChatClient.ChatClientRequestSpec buildPrompt(String query, String conversationId) {
        String normalizedQuery = normalize(query);
        StringBuilder bankingData = new StringBuilder();

        if (isListRequest(normalizedQuery)) {
            if (normalizedQuery.matches(".*\\bclients?\\b.*")) {
                bankingData.append("Clients actuels retournés par customer-service :\n")
                        .append(fetchData(customerServiceUrl).toPrettyString());
            }
            if (normalizedQuery.matches(".*\\bcomptes?\\b.*")) {
                if (!bankingData.isEmpty()) {
                    bankingData.append("\n\n");
                }
                bankingData.append("Comptes actuels retournés par ebank-service :\n")
                        .append(fetchData(accountServiceUrl).toPrettyString());
            }
        }

        if (!bankingData.isEmpty()) {
            return chatClient.prompt()
                    .system("Réponds à la demande en utilisant uniquement les données bancaires fournies. " +
                            "N'invente, ne modifie et ne complète aucune donnée. " +
                            "Respecte exactement le format demandé par l'utilisateur. " +
                            "S'il demande un tableau, réponds avec un tableau Markdown ; sinon, utilise le format demandé. " +
                            "Réponds en français.")
                    .user("Demande : " + query + "\n\nDonnées bancaires réelles :\n" + bankingData)
                    .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId));
        }

        return chatClient.prompt()
                .user(query)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId));
    }

    private JsonNode fetchData(String url) {
        String response = restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
        if (response == null) {
            throw new IllegalStateException("Le service bancaire a renvoyé une réponse vide : " + url);
        }
        try {
            JsonNode data = objectMapper.readTree(response);
            if (data == null || !data.isArray()) {
                throw new IllegalStateException("Le service bancaire n'a pas renvoyé une liste : " + url);
            }
            return data;
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Réponse JSON invalide du service bancaire : " + url, exception);
        }
    }

    private boolean isListRequest(String query) {
        return query.matches(".*\\b(liste|list|affich\\w*|montre\\w*|donne\\w*|recup\\w*|voir|consulte\\w*|tout|tous|toutes|quel|quels|quelles|all|show|display)\\b.*");
    }

    private String normalize(String query) {
        return Normalizer.normalize(query, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^\\p{L}\\p{N}]+", " ")
                .trim();
    }
}
