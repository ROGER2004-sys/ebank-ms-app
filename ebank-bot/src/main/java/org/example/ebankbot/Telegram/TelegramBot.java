package org.example.ebankbot.Telegram; // Adaptez selon votre package

import org.example.ebankbot.agents.ebankIAAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import jakarta.annotation.PostConstruct;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.token:}")
    private String telegramBotToken;

    @Value("${telegram.bot.username:EbankBot}")
    private String telegramBotUsername;

    private final ebankIAAgent aiAgent;

    public TelegramBot(ebankIAAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

    @Override
    public String getBotToken() {
        return telegramBotToken;
    }

    @Override
    public String getBotUsername() {
        return telegramBotUsername;
    }

    @PostConstruct
    public void registerTelegramBot() {
        if (telegramBotToken == null || telegramBotToken.isBlank()) {
            return;
        }

        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du bot Telegram", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Vérifie si un message texte a été reçu
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String userMessage = update.getMessage().getText();

            try {
                // Interroge l'agent IA (Spring AI) avec le message de l'utilisateur
                String aiResponse = aiAgent.chat(userMessage, chatId);

                // Envoie la réponse de l'IA sur Telegram
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText(aiResponse);
                execute(message);

            } catch (Exception e) {
                e.printStackTrace();
                SendMessage errorMessage = new SendMessage();
                errorMessage.setChatId(chatId);
                errorMessage.setText("Désolé, une erreur est survenue lors du traitement de votre demande.");
                try {
                    execute(errorMessage);
                } catch (TelegramApiException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}