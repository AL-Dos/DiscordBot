package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.awt.*;

public class InfoCommand implements Command {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Displays JavaBot's information";
    }

    @Override
    public void doSlashCommand(SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Info on JavaBot");
        eb.setDescription("JavaBot version 1.0! First iteration of a simple Discord bot.");
        eb.setColor(Color.GREEN);
        eb.addField("Author", "Two", true);
        eb.addField("Language", "Java", true);
        eb.addField("Libray", "Java Discord API", true);
        eb.setFooter("Created with more updates to come!");

        MessageEmbed embed = eb.build();
        event.replyEmbeds(embed).queue();
    }
}
