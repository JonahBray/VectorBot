package me.jonah.dialogue.types;

import me.jonah.dialogue.Dialogue;
import me.jonah.dialogue.questions.Question;
import me.jonah.lib.util.Color;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class AnnouncementDialogue implements Dialogue {
    protected final Question<java.awt.Color> embedColor;
    protected final Question<String> authorString;
    protected final Question<String> titleString;
    protected final Question<String> descriptionString;
    protected final Question<String> channelString;
    protected final String answerChannel;
    protected final String userID;
    protected final int questionID;
    protected final String cancelWord;

    public AnnouncementDialogue() {
        embedColor = new Question<>(colorQuestionEvent -> {
            colorQuestionEvent.getQuestion().setAnswer(Color.color(colorQuestionEvent.getValue()));
            colorQuestionEvent.getEvent().getChannel().sendMessage(
                    colorQuestionEvent.getQuestion().getDialogue().getNextQuestion()).queue();
        },
                colorQuestionEvent ->
                        colorQuestionEvent.getEvent().getChannel().sendMessage("That is not a valid color! Try again!").queue(), "What would you like the color of the embed to be?",
                colorQuestionEvent -> {
                    try {
                        Color.color(colorQuestionEvent.getValue());
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }, this);
    }

    public String getCancelWord() {
        return cancelWord;
    }

    public java.awt.Color getEmbedColor() {
        return embedColor.getAnswer();
    }

    public String getAuthorString() {
        return authorString.getAnswer();
    }

    public String getTitleString() {
        return titleString.getAnswer();
    }

    public String getDescriptionString() {
        return descriptionString.getAnswer();
    }

    public MessageEmbed getNextQuestion() {

    }

    public <T> Question<T> getCurrentQuestion(int questionID) {
        return null;
    }

    public int getQuestionID() {
        return questionID;
    }
}
