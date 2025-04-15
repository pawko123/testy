package org.example.mastermind;

public class Guess {
    private final String answer;
    private final boolean correct;

    public Guess(String answer, boolean correct) {
        this.answer = answer;
        this.correct = correct;
    }

    public String getAnswer(){
        return answer;
    }

    public boolean isCorrect() {
        return correct;
    }
}
