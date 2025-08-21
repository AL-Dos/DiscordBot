package commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class EchoCommand implements Command {
    @Override
    public String getName() {
        return "echo";
    }

    @Override
    public String getDescription() {
        return "Echoes back your text!";
    }

    @Override
    public void doSlashCommand(SlashCommandInteractionEvent event) {
        OptionMapping textOption = event.getOption("text");
        String textToEcho = "";
        if (textOption != null) {
            textToEcho = textOption.getAsString();
        }
        else {
            textToEcho = "You must provide a text!";
        }
        event.reply(textToEcho).setEphemeral(false).queue();
    }
}
