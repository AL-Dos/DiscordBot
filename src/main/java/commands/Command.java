package commands;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public interface Command {
    String getName();
    String getDescription();

    void doSlashCommand(SlashCommandInteractionEvent event);

}
