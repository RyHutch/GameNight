package com.example.ryan.gamenight.objects;

public class Die {

    private int numberOfFaces;
    private int currentFaceUp;

    public Die(int numberOfFaces){
        this.numberOfFaces = numberOfFaces;
        roll();
    }

    public void roll(){
        currentFaceUp = 1 + (int)(Math.random() * numberOfFaces);
    }

    public int getNumberOfFaces() {
        return numberOfFaces;
    }

    public void setNumberOfFaces(int numberOfFaces) {
        this.numberOfFaces = numberOfFaces;
    }

    public int getCurrentFaceUp() {
        return currentFaceUp;
    }
}