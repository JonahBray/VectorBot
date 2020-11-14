package me.jonah.dialogue;

import me.jonah.dialogue.questions.Question;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * @author Jonah Bray
 */
public interface Dialogue {
    MessageEmbed getNextQuestion();

    Question<?> getCurrentQuestion(int questionID);

    int getQuestionID();

    String getCancelWord();

    String getUserID();
}
