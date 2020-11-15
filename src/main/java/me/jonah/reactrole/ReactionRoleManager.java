package me.jonah.reactrole;

import me.jonah.VectorBot;
import me.jonah.lib.config.Config;
import me.jonah.reactrole.data.ReactionData;
import me.jonah.reactrole.data.ReactionMessage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

// todo: add support for emojis
public class ReactionRoleManager extends ListenerAdapter {
    private final Config<ReactionData> dataConfig;
    private final Set<ReactionMessage> loadedReactions;

    public ReactionRoleManager() {
        loadedReactions = new HashSet<>();
        dataConfig = VectorBot.getConfigManager().loadConfig("/data/reaction", new ReactionData());
        for (ReactionMessage reaction : dataConfig.getConfiguration().getReactions()) {
            loadReaction(reaction);
        }
    }

    public Config<ReactionData> getDataConfig() {
        return dataConfig;
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        if (event.getGuild().getId().equals(VectorBot.getBotConfig().getConfiguration().guildID)) {
            for (ReactionMessage loadedReaction : loadedReactions) {
                if (event.getMessageId().equals(loadedReaction.getMessageID())) {
                    if (!event.getReactionEmote().isEmoji() &&
                            event.getReactionEmote().getEmote().getId().equals(loadedReaction.getEmojiID())) {
                        final Role role = VectorBot.getJda().getRoleById(loadedReaction.getRoleID());
                        if (role == null) {
                            System.out.println("Error! Role is null!");
                            return;
                        }
                        if (event.getMember() == null) return;
                        event.getGuild().removeRoleFromMember(event.getMember(), role).queue();
                        break;
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (event.getGuild().getId().equals(VectorBot.getBotConfig().getConfiguration().guildID)) {
            for (ReactionMessage loadedReaction : loadedReactions) {
                if (event.getMessageId().equals(loadedReaction.getMessageID())) {
                    if (!event.getReactionEmote().isEmoji() &&
                            event.getReactionEmote().getEmote().getId().equals(loadedReaction.getEmojiID())) {
                        final Role role = VectorBot.getJda().getRoleById(loadedReaction.getRoleID());
                        if (role == null) {
                            System.out.println("Error! Role is null!");
                            return;
                        }
                        event.getGuild().addRoleToMember(event.getMember(), role).queue();
                        break;
                    } else {
                        event.getReaction().removeReaction(event.getUser()).queue();
                        break;
                    }
                }
            }
        }
    }

    public void loadReaction(ReactionMessage message) {
        final Role role = VectorBot.getJda().getRoleById(message.getRoleID());
        TextChannel channel = VectorBot.getJda().getTextChannelById(message.getChannelID());
        if (channel == null) {
            System.out.println("Error! Could not find channel!");
            return;
        }
        if (role == null) {
            System.out.println("Error! Role is null!");
            return;
        }
        final Guild guild = VectorBot.getJda().getGuildById(VectorBot.getBotConfig().getConfiguration().guildID);
        for (Member membersWithRole : guild.getMembersWithRoles(role)) {
            guild.removeRoleFromMember(membersWithRole, role).queue();
        }
        channel.retrieveReactionUsersById(message.getMessageID(), guild.getEmoteById(message.getEmojiID()))
                .forEachAsync(user -> {
                    guild.addRoleToMember(user.getId(), role).queue();
                    return true;
                });
        loadedReactions.add(message);
    }
}
