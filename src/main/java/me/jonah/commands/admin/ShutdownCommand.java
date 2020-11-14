package me.jonah.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.jonah.VectorBot;

/**
 * @author Jonah Bray
 */
public class ShutdownCommand extends Command {

    public ShutdownCommand(Category category) {
        this.category = category;
        this.name = "shutdown";
        this.help = "Bot go brrrrrr";
        this.guildOnly = false;
    }

    /**
     * The main body method of a {@link Command Command}.
     * <br>This is the "response" for a successful
     * {@link Command#run(CommandEvent) #run(CommandEvent)}.
     *
     * @param event The {@link CommandEvent CommandEvent} that
     *              triggered this Command
     */
    @Override
    protected void execute(CommandEvent event) {
        event.reactSuccess();
        VectorBot.getBotConfig().saveConfig();
        event.getJDA().shutdown();
        System.exit(0);
    }
}
