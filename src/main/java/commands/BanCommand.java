package commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class BanCommand implements Command {
    private static final String ADMIN_ROLE = "Admin";

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getDescription() {
        return "Ban a user (Admin Only Command)";
    }

    @Override
    public void doSlashCommand(SlashCommandInteractionEvent event) {
        Member adminUser = event.getMember();

        // checks if admin
        if (adminUser == null || adminUser.getRoles().stream().noneMatch(role -> role.getName().equalsIgnoreCase(ADMIN_ROLE))) {
            event.reply("You are not allowed to use this command!").setEphemeral(true).queue();
            return;
        }

        // Get user and reason for ban
        User targetUser = event.getOption("user") != null ? Objects.requireNonNull(event.getOption("user")).getAsUser() : null;
        String reason = event.getOption("reason") != null ? Objects.requireNonNull(event.getOption("reason")).getAsString() : "No reason provided";

        // checks if user is mention
        if (targetUser == null) {
            event.reply("No user was given.").setEphemeral(true).queue();
            return;
        }

        // check if self-ban
        if (targetUser.equals(adminUser.getUser())) {
            event.reply("You cannot ban yourself!").setEphemeral(true).queue();
            return;
        }

        // check if bot ban
        if (targetUser.isBot()) {
            event.reply("You cannot ban the bot!").setEphemeral(true).queue();
            return;
        }

        Objects.requireNonNull(event.getGuild()).ban(targetUser, 0, TimeUnit.DAYS).reason(reason)
                .queue(
                        _ -> event.reply("✅ " + targetUser.getAsTag() + " has been banned. Reason: " + reason).queue(),
                        _ -> event.reply("❌ Failed to ban " + targetUser.getAsTag() + ". Do I have permission?").setEphemeral(true).queue()
                );
    }
}
