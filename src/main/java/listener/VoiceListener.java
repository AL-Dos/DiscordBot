package listener;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VoiceListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        var userLeave = event.getChannelLeft();
        String channelId = "1407796709760700581";

        if (userLeave != null && userLeave.getMembers().stream().allMatch(m -> m.getUser().isBot())) {
            userLeave.getGuild().getAudioManager().closeAudioConnection();
            if (event.getGuild().getTextChannelById(channelId) != null) {
                Objects.requireNonNull(event.getGuild().getTextChannelById(channelId)).sendMessage("Everyone left, I'm disconnecting!").queue();
            }
        }
    }
}
