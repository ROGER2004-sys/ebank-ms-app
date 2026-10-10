package org.example.ebankbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.ebankbot.Discord.DiscordBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EbankBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBotApplication.class, args);
    }

    @Bean(destroyMethod = "shutdown")
    JDA discordJda(@Value("${discord.token}") String token, DiscordBot discordBot) {
        return JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(discordBot)
                .build();
    }
}
