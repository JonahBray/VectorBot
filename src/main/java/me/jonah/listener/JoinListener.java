package me.jonah.listener;

import com.jagrosh.jdautilities.examples.doc.Author;
import me.jonah.VectorBot;
import me.jonah.lib.util.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * @author  Jonah Bray
 */
public class JoinListener extends ListenerAdapter {
    // Event is fired whenever a member joins a guild we're in.
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (event.getGuild().getId().equals(VectorBot.getBotConfig().getConfiguration().guildID)) {
            TextChannel channel = event.getGuild().getTextChannelById(VectorBot.getBotConfig().getConfiguration().joinMessageChannel);
            if (channel != null) {
                if (channel.canTalk()) {

                    channel.sendMessage(
                            new EmbedBuilder()
                                    .setTitle(event.getMember().getAsMention())
                                    .setAuthor("Welcome to Vector 8177",
                                            VectorBot.getBotConfig().getConfiguration().discordInvite,
                                            event.getGuild().getIconUrl())
                                    .setFooter("Vector Robotics")
                                    .setTimestamp(Instant.now())
                                    .setDescription("**Meeting:** Every Friday\n**Information:** Please change your nickname to your real name\n**Members:** " + event.getGuild().getMemberCount() + "\n\n\n\n")
                                    .setThumbnail(event.getUser().getEffectiveAvatarUrl())
                                    .setColor(Color.color(VectorBot.getBotConfig().getConfiguration().joinMessageColor))
                                    .build()).queue();
                } else System.out.println("Can't talk in welcome channel!");
            } else System.out.println("Welcome channel is null!");
            Role role = event.getGuild().getRoleById(VectorBot.getBotConfig().getConfiguration().joinRole);
            if (role != null) {
                event.getGuild().addRoleToMember(event.getMember(), role).queue();
            }
        }
    }
}
