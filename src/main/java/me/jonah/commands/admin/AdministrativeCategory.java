package me.jonah.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sun.media.jfxmedia.logging.Logger;
import me.jonah.VectorBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author jonahbray
 */
public class AdministrativeCategory extends Command.Category {
    public AdministrativeCategory(String noPermission) {
        super("Administrative", noPermission, createPredicate());
    }


    private static Predicate<CommandEvent> createPredicate() {
        return commandEvent -> {
            if (commandEvent.isOwner())
                return true;
            Guild guild = commandEvent.getJDA().getGuildById(VectorBot.getBotConfig().getConfiguration().guildID);
            if (guild == null) {
                Logger.logMsg(Logger.ERROR, "Could not find vector discord! Was I kicked?");
                return false;
            }
            Member m = guild.getMember(commandEvent.getAuthor());
            if (m == null) {
                m = guild.retrieveMember(commandEvent.getAuthor()).complete();
            }

            if (m == null) {
                return false;
            }
            return m.getPermissions().contains(Permission.ADMINISTRATOR) || m.getRoles().stream().map(Role::getName)
                    .collect(Collectors.toList()).contains(VectorBot.getBotConfig().getConfiguration().administrativeRole);
        };
    }
}
