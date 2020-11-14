package me.jonah;

import com.google.gson.Gson;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import me.jonah.commands.admin.*;
import me.jonah.configuration.BotSettings;
import me.jonah.dialogue.DialogueManager;
import me.jonah.lib.config.Config;
import me.jonah.lib.config.ConfigManager;
import me.jonah.listener.DialogueListener;
import me.jonah.listener.JoinListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.Scanner;

/**
 * @author Jonah Bray
 */
public class VectorBot {
    private static final Gson gson = new Gson();
    private static JDA jda;
    private static ConfigManager configManager;
    private static Config<BotSettings> botConfig;
    private static DialogueManager dialogueManager;

    public static Gson getGson() {
        return gson;
    }


    public static DialogueManager getDialogueManager() {
        return dialogueManager;
    }

    public static void main(String[] args) throws InterruptedException {
        configManager = new ConfigManager();
        botConfig = configManager.loadConfig("/data/settings", new BotSettings());
        dialogueManager = new DialogueManager();

        CommandClientBuilder builder = new CommandClientBuilder();


        builder.setActivity(Activity.watching(botConfig.getConfiguration().status));

        setupBuilder(builder);
        // Bot can not start until we have the correct token.
        boolean successful = false;
        // Infinite loop until token is correct.
        while (!successful) {

            if (botConfig.getConfiguration().token == null) {
                // first start
                Scanner s = new Scanner(System.in);
                System.out.print("Please input your bot token: ");
                botConfig.getConfiguration().token = s.nextLine();
            }


            // JDA Is now ready for anything else such as stop commands in console.
            try {
                // We try and connect, if it throws an login error there was something wrong with token and as such
                // We try again.
                jda = JDABuilder.createDefault(args[0])
                        .setAutoReconnect(true)
                        .setMemberCachePolicy(MemberCachePolicy.ONLINE)
                        .enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
                        .addEventListeners(builder.build())
                        .addEventListeners(new JoinListener(),
                                new DialogueListener())
                        .build();
                successful = true;
            } catch (LoginException e) {
                e.printStackTrace();
                // Means invalid token, we should null our token that was saved to allow for it to input another one.
                if (e.getMessage().trim().equalsIgnoreCase("The provided token is invalid!")) {
                    botConfig.getConfiguration().token = null;
                }
                // We sleep for 1 second before trying to login again
                Thread.sleep(1000);
            }
        }
        // We save config here because we know the token is correct if it was ever null.
        botConfig.saveConfig();
    }

    public static JDA getJda() {
        return jda;
    }

    public static Config<BotSettings> getBotConfig() {
        return botConfig;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }


    private static void setupBuilder(CommandClientBuilder builder) {
        builder.setEmojis(botConfig.getConfiguration().emojis[0], botConfig.getConfiguration().emojis[1], botConfig.getConfiguration().emojis[2]);

        builder.setPrefix(botConfig.getConfiguration().commandPrefix);
        builder.setAlternativePrefix(botConfig.getConfiguration().alternativeCommandPrefix);
        builder.setOwnerId(botConfig.getConfiguration().ownerID);
        builder.setCoOwnerIds(botConfig.getConfiguration().admins);

        builder.useHelpBuilder(true);
        builder.setHelpConsumer(new HelpCommand());

        Command.Category admin = new AdministrativeCategory(
                botConfig.getConfiguration().emojis[2] + " You do not have permission to use this command!");

        builder.addCommands(
                new PingCommand(admin),
                new AnnounceCommand(admin),
                new ReloadCommand(admin),
                new ShutdownCommand(admin));

    }
}
