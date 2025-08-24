package member_alerts;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateMaxMembersEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class MemberAlerts extends ListenerAdapter {
    private final List<String> messages = Arrays.asList(
        "Hello there! You have join to the dark side! %s, welcome to the server!",
        "You joined just in time %s, come get soup.",
        "LMAO, some random just joined the server. %s, weird name.",
        "Damn nice ass %s! Welcome to the owner's own Diddy Server!"
    );
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String userMention = event.getUser().getAsMention();
        int index = (int) (Math.random() * messages.size());
        String welcomeMessage = String.format(messages.get(index), userMention);
        Objects.requireNonNull(event.getGuild().getDefaultChannel()).asTextChannel().sendMessage(welcomeMessage).queue();

    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        TextChannel targetChannel = Objects.requireNonNull(event.getGuild().getDefaultChannel()).asTextChannel();
        targetChannel.sendMessage(event.getUser().getAsTag() + " left the server!").queue();
    }

    @Override
    public void onGuildUpdateMaxMembers(@NotNull GuildUpdateMaxMembersEvent event) {
        TextChannel targetChannel = Objects.requireNonNull(event.getGuild().getDefaultChannel()).asTextChannel();
        targetChannel.sendMessage("Server is full! Can't accommodate any more users!").queue();
    }

    @Override
    public void onGuildBan (@NotNull GuildBanEvent event) {
        TextChannel targetChannel = Objects.requireNonNull(event.getGuild().getDefaultChannel()).asTextChannel();
        targetChannel.sendMessage(event.getUser().getAsTag() + "You have been banned!").queue();
    }
}
