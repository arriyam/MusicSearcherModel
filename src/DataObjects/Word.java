package DataObjects;

import java.io.Serializable;

public class Word implements Serializable {
    private String word;
    private double idfVal;

    public Word(){
        this.word = "";
        this.idfVal = 0.0;
    }

    public Word(String word){
        this.word = word;
        this.idfVal = 0.0;
    }
    public Word(String word,double idfVal){
        this.word = word;
        this.idfVal = idfVal;
    }
    public String getWord() {
        return word;
    }



    public double getIdfVal() {
        return idfVal;
    }

    public void setIdfVal(double idfVal) {
        this.idfVal = idfVal;
    }

}
