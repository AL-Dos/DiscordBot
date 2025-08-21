package commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PingCommand implements Command {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Checks JavaBot's latency to Discord gateway";
    }

    @Override
    public void doSlashCommand(SlashCommandInteractionEvent event) {
        long ping = event.getJDA().getGatewayPing();
        event.replyFormat("Gateway ping: %dms", ping ).queue();
    }
}
