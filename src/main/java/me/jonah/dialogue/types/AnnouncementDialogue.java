package me.jonah.dialogue.types;

import me.jonah.dialogue.Dialogue;
import me.jonah.dialogue.questions.Question;
import me.jonah.lib.util.Color;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class AnnouncementDialogue implements Dialogue {
    protected final Question<java.awt.Color> embedColor;
    protected final Question<String> authorString;
    protected final Question<String> titleString;
    protected final Question<String> descriptionString;
    protected final Question<TextChannel> channelString;
    protected final String userID;
    protected final int questionID;
    protected final String cancelWord;

    public AnnouncementDialogue(final String userID) {
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
        authorString = new Question<>(stringQuestionEvent -> {
            stringQuestionEvent.getQuestion().setAnswer(stringQuestionEvent.getValue());
            stringQuestionEvent.getEvent().getChannel().sendMessage(
                    stringQuestionEvent.getQuestion().getDialogue().getNextQuestion()
            ).queue();
        }, stringQuestionEvent -> stringQuestionEvent.getEvent().getChannel().sendMessage("That is not a valid string! Try again!").queue(),
                "What do you wish for the author field to be?", stringQuestionEvent -> true, this);
        titleString = new Question<>(stringQuestionEvent -> {
            stringQuestionEvent.getQuestion().setAnswer(stringQuestionEvent.getValue());
            stringQuestionEvent.getEvent().getChannel().sendMessage(
                    stringQuestionEvent.getQuestion().getDialogue().getNextQuestion()
            ).queue();
        }, stringQuestionEvent -> stringQuestionEvent.getEvent().getChannel().sendMessage("That is not a valid string! Try again!").queue(),
                "What do you wish for the title field to be?", stringQuestionEvent -> true, this);
        descriptionString = new Question<>(stringQuestionEvent -> {
            stringQuestionEvent.getQuestion().setAnswer(stringQuestionEvent.getValue());
            stringQuestionEvent.getEvent().getChannel().sendMessage(
                    stringQuestionEvent.getQuestion().getDialogue().getNextQuestion()
            ).queue();
        }, stringQuestionEvent -> stringQuestionEvent.getEvent().getChannel().sendMessage("That is not a valid string! Try again!").queue(),
                "What do you wish for the description field to be?", stringQuestionEvent -> true, this);
        channelString = new Question<>(stringQuestionEvent -> {
            stringQuestionEvent.getQuestion().setAnswer(stringQuestionEvent.getEvent().getJDA().getTextChannelById(stringQuestionEvent.getValue()));
            stringQuestionEvent.getEvent().getChannel().sendMessage(
                    stringQuestionEvent.getQuestion().getDialogue().getNextQuestion()
            ).queue();
        }, stringQuestionEvent -> stringQuestionEvent.getEvent().getChannel().sendMessage("That is not a valid channel! Try again!").queue(),
                "What do you wish for the channel to be sent in (ID)?", stringQuestionEvent -> stringQuestionEvent.getEvent().getJDA().getTextChannelById(stringQuestionEvent.getValue()) != null, this);
        this.userID = userID;
        this.questionID = 0;
        this.cancelWord = "cancel";
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

    public String getUserID() {
        return userID;
    }

    public String getTitleString() {
        return titleString.getAnswer();
    }

    public String getDescriptionString() {
        return descriptionString.getAnswer();
    }

    public MessageEmbed getNextQuestion() {
        return null;
    }

    public <T> Question<T> getCurrentQuestion(int questionID) {
        return null;
    }

    public int getQuestionID() {
        return questionID;
    }
}
