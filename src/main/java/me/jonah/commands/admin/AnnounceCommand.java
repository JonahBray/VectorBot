package me.jonah.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.jonah.VectorBot;
import me.jonah.dialogue.types.AnnouncementDialogue;

/**
 * @author Jonah Bray
 */
public class AnnounceCommand extends Command {
    public AnnounceCommand(Category category) {
        this.category = category;
        this.name = "announce";
        this.help = "Announce to the peasants.";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getGuild().getId().equals(VectorBot.getBotConfig().getConfiguration().guildID)) {
            AnnouncementDialogue dialogue = new AnnouncementDialogue(event.getMember().getUser().getId());
            VectorBot.getDialogueManager().addDialogue(dialogue);
            event.reply(dialogue.getNextQuestion());

        } else event.replyError("You must use this command in Vector's dicsord server!");
    }
}
