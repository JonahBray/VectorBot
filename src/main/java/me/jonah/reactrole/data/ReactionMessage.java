package me.jonah.reactrole.data;

public class ReactionMessage {
    private final String messageID;
    private final String roleID;
    private final String emojiID;
    private final String channelID;

    public String getChannelID() {
        return channelID;
    }

    public ReactionMessage(String messageID, String roleID, String emojiID, String channelID) {
        this.messageID = messageID;
        this.roleID = roleID;
        this.emojiID = emojiID;
        this.channelID = channelID;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getRoleID() {
        return roleID;
    }

    public String getEmojiID() {
        return emojiID;
    }
}
