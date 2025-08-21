package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Refactor code if you want to insert your token to the config file
public class BotConfig {
    private static final Logger log = LoggerFactory.getLogger(BotConfig.class);

    public static String getBotToken() {
        String botToken = System.getenv("botToken");
        if (botToken == null || botToken.isEmpty()) {
            log.error("Bot token is not found in the environment variables!");
        }
        return botToken;
    }
}
