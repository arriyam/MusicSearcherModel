package Model;

import Blueprint.SearchResult;

public class Result implements SearchResult,Comparable<Result>{

    //Data stored in the object
    private String title;
    private double score;
    private String url;

    //Constructor
    public Result(String title,String url, double score){
        this.title = title;
        this.url = url;
        this.score = score;
    }

    //Get and set methods
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //Compare to method for the treeset to make use of
    public int compareTo(Result result){
        double roundOff1 = Math.round((this.getScore() * 1000.0)) / 1000.0;
        double roundoff2 = Math.round((result.getScore()*1000.0)) / 1000.0;

        if (this.getUrl() == result.getUrl()){
            return 0;
        }
        else if (roundOff1 > roundoff2){
            return -1;
        }
        else if(roundOff1 == roundoff2){
            return ((this.getTitle().compareTo(result.getTitle())));
        }
        else if(roundOff1 < roundoff2){
            return 1;
        }
        else{
            return 0;
        }
    }

    public boolean equals(Result result){
        if(!(result instanceof Result))return false;
        return result.equals(((Result)result).getUrl());
    }

    public String toString() {
        return "Score: " + this.getScore()+ ", Title: " + this.getTitle();
    }
}
