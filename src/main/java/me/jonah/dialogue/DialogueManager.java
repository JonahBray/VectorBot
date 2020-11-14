package me.jonah.dialogue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jonah Bray
 */
public class DialogueManager {
    private final Map<String, Dialogue> userDialogueMap;

    public DialogueManager() {
        userDialogueMap = new HashMap<>();
    }

    public Map<String, Dialogue> getUserDialogueMap() {
        return userDialogueMap;
    }

    public void addDialogue(Dialogue dialogue) {
        userDialogueMap.put(dialogue.getUserID(), dialogue);
    }
}
