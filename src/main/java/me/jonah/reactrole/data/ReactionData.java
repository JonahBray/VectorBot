package me.jonah.reactrole.data;

import java.util.ArrayList;
import java.util.List;

public class ReactionData {
    private final List<ReactionMessage> reactions;

    public ReactionData() {
        reactions = new ArrayList<>();
    }

    public List<ReactionMessage> getReactions() {
        return reactions;
    }
}
