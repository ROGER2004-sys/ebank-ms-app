package org.example.ebankbot.Discord;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.ebankbot.agents.ebankIAAgent;
import org.springframework.stereotype.Component;

@Component
public class DiscordBot extends ListenerAdapter {
    private final ebankIAAgent ebankAIAgent;

    public DiscordBot(ebankIAAgent ebankAIAgent) {
        this.ebankAIAgent = ebankAIAgent;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        String query = event.getMessage().getContentRaw();
        String response = ebankAIAgent.chat(query, event.getAuthor().getId());
        event.getChannel().sendMessage(response).queue();
    }
}
