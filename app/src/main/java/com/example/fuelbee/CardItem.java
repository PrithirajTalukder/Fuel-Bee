package com.example.fuelbee;

public class CardItem {

    private int cardImage;
    private String cardTitle;
    private String cardDistance;

    public int getCardImage() {
        return cardImage;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public String getCardDistance() {
        return cardDistance;
    }

    public CardItem(int cardImage, String cardTitle, String cardDistance) {
        this.cardImage = cardImage;
        this.cardTitle = cardTitle;
        this.cardDistance = cardDistance;
    }
}
