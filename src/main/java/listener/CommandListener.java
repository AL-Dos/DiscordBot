package listener;

import commands.Command;
import commands.EchoCommand;
import commands.InfoCommand;
import commands.PingCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class CommandListener extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(CommandListener.class);
    private final Map<String, Command> commands = new HashMap<>();

    public CommandListener() {
        commands.put("ping", new PingCommand());
        commands.put("info", new InfoCommand());
        commands.put("echo", new EchoCommand());
        log.info("Registered {} commands", commands.size());
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        log.info("JDA has been initialized! Logged in as {}#{}",
                event.getJDA().getSelfUser().getName(),
                event.getJDA().getSelfUser().getId());
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String  commandName = event.getName();
        Command cmd = commands.get(commandName);

        if (cmd != null) {
            log.debug("Unknown command {}, from user: {}", commandName, event.getUser().getName());
            cmd.doSlashCommand(event);
        }
        else {
            log.warn("Unknown command {}, from user: {}", commandName, event.getUser().getName());
            event.reply("Unknown command: " + commandName).setEphemeral(true).queue();
        }
    }
}
