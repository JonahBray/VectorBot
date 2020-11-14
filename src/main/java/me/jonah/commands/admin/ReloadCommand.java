package me.jonah.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.jonah.VectorBot;

/**
 * @author Jonah Bray
 */
public class ReloadCommand extends Command {
    public ReloadCommand(Category category) {
        this.category = category;
        this.name = "reload";
        this.help = "Reload bot configuration.";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.reactSuccess();
        VectorBot.getBotConfig().saveConfig();
        VectorBot.getBotConfig().reloadConfig();
    }
}
