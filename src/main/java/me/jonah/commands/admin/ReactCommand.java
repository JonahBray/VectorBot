package me.jonah.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.jonah.VectorBot;
import me.jonah.dialogue.Dialogue;
import me.jonah.reactrole.ReactDialogue;

public class ReactCommand extends Command {
    public ReactCommand(Category category) {
        this.category = category;
        this.name = "react";
        this.help = "stonks go up when people react";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getGuild().getId().equals(VectorBot.getBotConfig().getConfiguration().guildID)) {
            Dialogue dialogue = new ReactDialogue(commandEvent.getMember().getUser().getId(), VectorBot.getReactionRoleManager());
            VectorBot.getDialogueManager().addDialogue(dialogue);
            commandEvent.reply(dialogue.getNextQuestion());

        } else commandEvent.replyError("You must use this command in Vector's dicsord server!");
    }
}
