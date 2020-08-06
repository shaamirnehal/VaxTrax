package com.example.vaxtrax;


//  model for answers for quiz one. Quiz one has four options to questions.


public class AnsOneModel {

    private int questionId;
    private boolean optionOne;
    private boolean optionTwo;
    private boolean optionThree;
    private boolean optionFour;

    public AnsOneModel(int questionId, boolean optionOne, boolean optionTwo, boolean optionThree, boolean optionFour) {
        this.questionId = questionId;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.optionThree = optionThree;
        this.optionFour = optionFour;
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

    public boolean isOptionThree() {
        return optionThree;
    }

    public void setOptionThree(boolean optionThree) {
        this.optionThree = optionThree;
    }

    public boolean isOptionFour() {
        return optionFour;
    }

    public void setOptionFour(boolean optionFour) {
        this.optionFour = optionFour;
    }
}
