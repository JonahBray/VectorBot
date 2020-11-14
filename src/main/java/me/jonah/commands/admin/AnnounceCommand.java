package me.jonah.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.jonah.VectorBot;
import net.dv8tion.jda.api.EmbedBuilder;

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
//            event.reply(new EmbedBuilder()
//                    .setTitle("Color")
//                    .setAuthor("Announcement Creator",
//                            VectorBot.getBotConfig().getConfiguration().discordInvite,
//                            event.getGuild().getIconUrl())
//                    .setFooter("VectorBot")
//                    .setDescription("Please pick a color")
//                    .build());

        } else event.replyError("You must use this command in Vector's dicsord server!");
    }
}
