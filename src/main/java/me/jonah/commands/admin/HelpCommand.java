package me.jonah.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.jonah.VectorBot;
import me.jonah.lib.util.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Jonah Bray
 */
public class HelpCommand implements Consumer<CommandEvent> {

    @Override
    public void accept(CommandEvent commandEvent) {
        if (commandEvent.isFromType(ChannelType.TEXT))
            commandEvent.getMessage().delete().queue();
        commandEvent.replyInDm(new EmbedBuilder()
                .setAuthor("Vector", VectorBot.getBotConfig().getConfiguration().discordInvite,
                        commandEvent.getSelfUser().getEffectiveAvatarUrl())
                .setFooter("Help Message")
                .setColor(Color.color(VectorBot.getBotConfig().getConfiguration().helpColor))
                .setDescription(formatCommands(commandEvent.getClient().getCommands(), commandEvent))
                .build(), success -> {
        }, fail -> commandEvent.replyWarning("Could not send you help because you are blocking Direct Messages."));
    }

    private String formatCommands(List<Command> commands, CommandEvent event) {
        StringBuilder builder = new StringBuilder();


        Command.Category category = null;
        for (Command command : commands) {
            if (!command.isHidden() && ((category != null ? category.test(event) : !command.isOwnerCommand()) || event.isOwner())) {
                if (!Objects.equals(category, command.getCategory())) {
                    category = command.getCategory();
                    if (category == null || category.test(event))
                        builder.append("\n\n  __").append(category == null ? "No Category" : category.getName()).append("__:\n");
                }
                if (category == null || category.test(event))
                    builder.append("\n`").append(event.getClient().getTextualPrefix()).append(event.getClient().getPrefix() == null ? " " : "").append(command.getName())
                            .append(command.getArguments() == null ? "`" : " " + command.getArguments() + "`")
                            .append(" - ").append(command.getHelp());
            }
        }
        return builder.toString();
    }
}
