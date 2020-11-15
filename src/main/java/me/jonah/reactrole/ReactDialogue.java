package me.jonah.reactrole;

import me.jonah.VectorBot;
import me.jonah.dialogue.Dialogue;
import me.jonah.dialogue.questions.Question;
import me.jonah.lib.util.Color;
import me.jonah.reactrole.data.ReactionMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class ReactDialogue implements Dialogue {
    protected final Question<java.awt.Color> embedColor;
    protected final Question<String> authorString;
    protected final Question<String> titleString;
    protected final Question<String> descriptionString;
    protected final Question<TextChannel> channelString;
    protected final Question<Role> roleQuestion;
    protected final Question<Emote> emoji;
    protected final String userID;
    protected final String cancelWord;
    protected final ReactionRoleManager manager;
    protected int questionID;

    public ReactDialogue(final String userID, final ReactionRoleManager manager) {
        this.manager = manager;
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
        emoji = new Question<>(emojiQuestionEvent -> {
            emojiQuestionEvent.getQuestion().setAnswer(VectorBot.getJda().getEmotesByName(emojiQuestionEvent.getValue(), true).get(0));
            emojiQuestionEvent.getEvent().getChannel().sendMessage(emojiQuestionEvent.getQuestion().getDialogue().getNextQuestion()).queue();
        }, emojiQuestionEvent -> emojiQuestionEvent.getEvent().getChannel().sendMessage("That is not a valid emoji! Try again!").queue(),
                "What would you like the users to react to?", emojiQuestionEvent -> !VectorBot.getJda().getEmotesByName(emojiQuestionEvent.getValue(), true).isEmpty(), this);
        roleQuestion = new Question<>(event -> {
            event.getQuestion().setAnswer(VectorBot.getJda().getRolesByName(event.getValue(), true).get(0));
            event.getEvent().getChannel().sendMessage(event.getQuestion().getDialogue().getNextQuestion()).queue();
        }, event -> event.getEvent().getChannel().sendMessage("That is not a valid role").queue(),
                "Please choose a role to use.", event -> !VectorBot.getJda().getRolesByName(event.getValue(), true).isEmpty(), this);
        channelString = new Question<>(stringQuestionEvent -> {
            // For clarification, channelString is the last question in the list of questions, as such once it's completed,
            // it will send the announcement.
            stringQuestionEvent.getQuestion().setAnswer(stringQuestionEvent.getEvent().getJDA().getTextChannelById(stringQuestionEvent.getValue()));
            sendMessage();
            VectorBot.getDialogueManager().getUserDialogueMap().remove(userID);
        }, stringQuestionEvent -> stringQuestionEvent.getEvent().getChannel().sendMessage("That is not a valid channel! Try again!").queue(),
                "What do you wish for the channel to be sent in (ID)?", stringQuestionEvent -> {
            try {
                return stringQuestionEvent.getEvent().getJDA().getTextChannelById(stringQuestionEvent.getValue()) != null;
            } catch (Exception e) {
                return false;
            }
        }, this);
        this.userID = userID;
        // For clarification, we start at -1, so when someone does the command for the first time, it gets the first question instead of the second.
        this.questionID = -1;
        this.cancelWord = "cancel";
    }

    public Role getRoleQuestion() {
        return roleQuestion.getAnswer();
    }

    public Emote getEmoji() {
        return emoji.getAnswer();
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

    public TextChannel getChannelString() {
        return channelString.getAnswer();
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
        questionID++;
        return new EmbedBuilder()
                .setAuthor("Reaction Role Creator")
                .setTitle(getCurrentQuestion(questionID).getQuestion())
                .build();
    }

    public Question<?> getCurrentQuestion(int questionID) {
        switch (questionID) {
            case 0:
                return embedColor;
            case 1:
                return authorString;

            case 2:
                return titleString;

            case 3:
                return descriptionString;
            case 4:
                return roleQuestion;
            case 5:
                return emoji;
            case 6:
                return channelString;

        }
        return null;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void sendMessage() {
        getChannelString().sendMessage(new EmbedBuilder()
                .setTitle(getTitleString())
                .setColor(getEmbedColor())
                .setDescription(getDescriptionString())
                .setAuthor(getAuthorString(), VectorBot.getBotConfig().getConfiguration().discordInvite,
                        VectorBot.getJda().getSelfUser().getEffectiveAvatarUrl())
                .setFooter("React to this message to get the role.")
                .build()).queue(message -> {
            message.addReaction(getEmoji()).queue();
            ReactionMessage reactionMessage = new ReactionMessage(message.getId(),
                    getRoleQuestion().getId(), getEmoji().getId(), getChannelString().getId());
            manager.getDataConfig().getConfiguration().getReactions().add(reactionMessage);
            manager.getDataConfig().saveConfig();
            manager.loadReaction(reactionMessage);
        });
    }
}
