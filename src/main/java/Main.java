import config.BotConfig;
import listener.CommandListener;
import listener.VoiceListener;
import member_alerts.MemberAlerts;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.EnumSet;
import java.util.Objects;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static JDA jda;

    public static void main(String[] args) throws Exception {
        String botToken = BotConfig.getBotToken();

        try {
            jda = JDABuilder.createDefault(botToken)
                    .enableIntents(EnumSet.allOf(GatewayIntent.class))
                    .addEventListeners(new CommandListener(), new MemberAlerts(), new VoiceListener())
                    .build();
            jda.awaitReady();
            log.info("Bot is ready!");

            registerSlashCommands();
        } catch (Exception e) {
            log.error("There was an error initializing the bot!", e);
        }
    }

    private static void registerSlashCommands() {
        if (jda == null) {
            log.error("JDA has not been initialized!");
            return;
        }

        log.info("Registering slash commands...");
        String guildID = "1407796708741611730";
        Objects.requireNonNull(jda.getGuildById(guildID))
                .updateCommands()
                .addCommands(
                        Commands.slash("ping", "Checks the latency for JavaBot's Discord gateway."),
                        Commands.slash("info", "Displays the information of JavaBot."),
                        Commands.slash("echo", "Echoes your own message.")
                                .addOption(OptionType.STRING,"text", "This is an echo.", true),
                        Commands.slash("music", "Play music through one of voice channels.")
                                .addOption(OptionType.STRING,"url", "Youtube link or other link.", true),
                        Commands.slash("ban", "Ban a user from the server.")
                                .addOption(OptionType.USER, "user", "The user to ban", true)
                                .addOption(OptionType.STRING, "reason", "Reason for ban", false)
                )
                .queue(_ -> log.info("Slash commands successfully registered!"),
                        failed -> log.error("Slash commands failed: ", failed)
                );
    }
}
