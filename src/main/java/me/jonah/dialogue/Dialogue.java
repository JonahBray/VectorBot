package me.jonah.dialogue;

import me.jonah.dialogue.questions.Question;
import net.dv8tion.jda.api.entities.MessageEmbed;

public interface Dialogue {
    MessageEmbed getNextQuestion();

    <T> Question<T> getCurrentQuestion(int questionID);

    int getQuestionID();

    String getCancelWord();
}
