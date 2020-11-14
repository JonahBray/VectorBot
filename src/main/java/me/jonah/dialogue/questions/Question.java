package me.jonah.dialogue.questions;

import me.jonah.dialogue.Dialogue;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Question<T> {
    protected final Consumer<QuestionEvent<T>> success, failure;
    protected final String question;
    protected final Predicate<QuestionEvent<T>> isValid;
    protected final Dialogue dialogue;
    protected T answer;

    public Question(Consumer<QuestionEvent<T>> success, Consumer<QuestionEvent<T>> failure, String question, Predicate<QuestionEvent<T>> isValid, Dialogue dialogue) {
        this.success = success;
        this.failure = failure;
        this.question = question;
        this.isValid = isValid;
        this.dialogue = dialogue;
    }

    public Consumer<QuestionEvent<T>> getSuccess() {
        return success;
    }

    public Consumer<QuestionEvent<T>> getFailure() {
        return failure;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isValid(QuestionEvent<T> event) {
        return isValid.test(event);
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public T getAnswer() {
        return answer;
    }

    public void setAnswer(T answer) {
        this.answer = answer;
    }
}
