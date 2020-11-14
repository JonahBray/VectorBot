package me.jonah.listener;

import me.jonah.VectorBot;
import me.jonah.dialogue.Dialogue;
import me.jonah.dialogue.questions.Question;
import me.jonah.dialogue.questions.QuestionEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Jonah Bray
 */
public class DialogueListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getGuild().getId().equals(VectorBot.getBotConfig().getConfiguration().guildID)) {
            // We get and then check for null, checking if the map contains an item is more "pretty" but takes
            // 2x the processing power (however little that may be) as you're checking doing the same thing below
            // when you call hasKey, and then you get it again.
            Dialogue dialogue = VectorBot.getDialogueManager().getUserDialogueMap().get(event.getAuthor().getId());
            if (dialogue != null) {
                // Map has a dialogue of this user.
                if (dialogue.getCancelWord().equals(dialogue.getCancelWord())) {
                    VectorBot.getDialogueManager().getUserDialogueMap().remove(event.getAuthor().getId());
                    event.getChannel().sendMessage("Cancelled action.").queue();
                    return;
                }
                Question<?> question = dialogue.getCurrentQuestion(dialogue.getQuestionID());
                QuestionEvent<?> questionEvent = new QuestionEvent<>(event, event.getMessage().getContentRaw(), question);
                if (question.isValid(questionEvent))
                    question.getSuccess(questionEvent);
                else question.getFailure(questionEvent);
            }
        }
    }
}
