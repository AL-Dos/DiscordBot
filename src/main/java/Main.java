import config.BotConfig;
import listener.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.EnumSet;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static JDA jda;

    public static void main(String[] args) throws Exception {
        String botToken = BotConfig.getBotToken();

        try {
            jda = JDABuilder.createDefault(botToken)
                    .enableIntents(EnumSet.allOf(GatewayIntent.class))
                    .addEventListeners(new CommandListener())
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
        jda.updateCommands()
                .addCommands(
                        Commands.slash("ping", "Checks the latency for JavaBot's Discord gateway."),
                        Commands.slash("info", "Displays the information of JavaBot."),
                        Commands.slash("echo", "Echoes your own message")
                                .addOption(OptionType.STRING,"text", "This is an echo", true)
                )
                .queue(succes -> log.info("Slash commands successfully registered!"),
                        failed -> log.error("Slash commands failed: ", failed)
                );
    }
}
