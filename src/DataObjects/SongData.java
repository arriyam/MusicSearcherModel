package DataObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

    public class SongData implements Serializable {
        private String title;
        private Map<String,Integer> uniqueWordsInSong;
        private String artist;
        private int id;
        private Map<String,Double> tfVals;
        private Map<String,Double> tfIdfVals;

        public SongData(){
            this.title = "";
            this.uniqueWordsInSong = new HashMap<String,Integer>();
            this.artist = "";
            this.id = -1;
            this.tfVals = new HashMap<String,Double>();
            this.tfIdfVals = new HashMap<String,Double>();
        }


        public SongData(String title, HashMap<String, Integer> uniqueWordsInSong, String artist, int id, HashMap<String, Double> tfVals, HashMap<String,Double> tfIdfVals) {
            this.title = title;
            this.uniqueWordsInSong = uniqueWordsInSong;
            this.artist = artist;
            this.id = id;
            this.tfVals = tfVals;
            this.tfIdfVals = tfIdfVals;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Map<String, Integer> getUniqueWordsInSong() {
            return uniqueWordsInSong;
        }

        public void setUniqueWordsInSong(HashMap<String, Integer> uniqueWordsInPage) {
            this.uniqueWordsInSong = uniqueWordsInPage;
        }

        public void addWord(String word){
            if(uniqueWordsInSong.containsKey(word)){
                uniqueWordsInSong.put(word,uniqueWordsInSong.get(word)+1);
            }
            else{
                uniqueWordsInSong.put(word,1);
            }
        }

        public String getUrl() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }




        public Map<String, Double> getTfVals() {
            return tfVals;
        }

        public void setTfVals(HashMap<String, Double> tfVals) {
            this.tfVals = tfVals;
        }

        public Map<String, Double> getTfIdfVals() {
            return tfIdfVals;
        }
//
        public void setTfIdfVals(Map<String, Double> tfIdfVals) {
            this.tfIdfVals = tfIdfVals;
        }

        public void addTFVal(String word,double value){tfVals.put(word,value);}
    }

