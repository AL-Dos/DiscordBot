package commands;

import audio.PlayerManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;
import java.util.concurrent.*;

public class MusicCommand implements Command {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ConcurrentHashMap<Long, ScheduledFuture<?>> timeouts = new ConcurrentHashMap<>();

    @Override
    public String getName() {
        return "music";
    }

    @Override
    public String getDescription() {
        return "Play music through one of the voice channels. Bot leaves if no user detected within 30 minutes.";
    }

    @Override
    public void doSlashCommand(SlashCommandInteractionEvent event) {
        Member member = event.getMember();

        if (member == null || member.getVoiceState() == null || !member.getVoiceState().inAudioChannel()) {
            event.reply("Join a voice channel first before running the command").setEphemeral(true).queue();
            return;
        }

        AudioChannelUnion channel = Objects.requireNonNull(Objects.requireNonNull(member).getVoiceState()).getChannel();
        if (channel == null) {
            event.reply("Could not detect voice channel").setEphemeral(true).queue();
            return;
        }

        AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();
        audioManager.openAudioConnection(channel);

        audioManager.setSendingHandler(
                PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).getSendHandler()
        );

        String url = event.getOption("url") != null ? Objects.requireNonNull(event.getOption("url")).getAsString() : null;

        event.deferReply().setEphemeral(true).queue();

        if (url != null) {
            PlayerManager playerManager = PlayerManager.getInstance();
            playerManager.loadAndPlay(event.getGuild(), url);
            event.reply("Playing:  " + url).queue();
        }
        else {
            event.reply("Please provide a url.").setEphemeral(true).queue();
        }

        long guildId = event.getGuild().getIdLong();
        if (timeouts.containsKey(guildId)) {
            timeouts.get(guildId).cancel(true);
        }

        ScheduledFuture<?> task = scheduler.schedule(() -> {
            if (channel.getMembers().stream().allMatch(m -> m.getUser().isBot())) {
                audioManager.closeAudioConnection();
                event.getChannel().sendMessage("I'll leave the voice channel in 30 minutes due to inactivity!").queue();
            }
        }, 30, TimeUnit.MINUTES);

        timeouts.put(guildId, task);
    }
}
