package com.example.vaxtrax;

public class AnsTwoModel {

    private int questionId;
    private boolean optionOne;
    private boolean optionTwo;

    public AnsTwoModel(int questionId, boolean optionOne, boolean optionTwo) {
        this.questionId = questionId;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public boolean isOptionOne() {
        return optionOne;
    }

    public void setOptionOne(boolean optionOne) {
        this.optionOne = optionOne;
    }

    public boolean isOptionTwo() {
        return optionTwo;
    }

    public void setOptionTwo(boolean optionTwo) {
        this.optionTwo = optionTwo;
    }
}
