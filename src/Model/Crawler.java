package Model;

import DataObjects.SongData;
import DataObjects.Word;
import Tools.FileModifier;
import Tools.MatMult;
import Tools.SongParser;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;

//Test comment
public class Crawler {
    private Map<String, SongData> dataObjs;
    private Map<String, Word> wordObjs;
    private int id;
    private Map<Integer,String> idToSong;
    private Map<String,Integer> uniqueWords;
    private FileModifier fileModifier;

    public Crawler(){
        dataObjs = new HashMap<String, SongData>();
        wordObjs = new HashMap<String, Word>();
        id = 0;
        idToSong = new HashMap<Integer,String>();
        uniqueWords = new HashMap<String,Integer>();
        fileModifier = new FileModifier();
    }

//    The crawl function is accepts a seed url and crawls through the web content. It uses a List to go through all the websites the seed is connected to.
    public int crawl(String folderName){

        fileModifier.initialize();
        String parentDir = fileModifier.getParentDir();

        String path = parentDir + File.separator + "SongLyrics";
        File file = new File(path);
        String[] entries = file.list();
        if (entries != null) {
            for (String s : entries) {
                try {
                    File currentFile = new File(file.getPath(), s);
                    Path fileName = Path.of(currentFile.getPath());
                    String lyric = Files.readString(fileName);
                    //Song being parsed
                    SongParser songParser = new SongParser(lyric,s);
                    String title = songParser.parseTitle();
                    String artist = songParser.parseArtist();
                    ArrayList<String> words = songParser.parseWords();
//                    System.out.println(words);
                    // Taking the data and using calculating values needed for the search
                    HashMap<String,Integer> uniqueWordsInSong = createUniqueWordsInSong(words);
                    HashMap<String,Double> tfVals = createTfVals(words,uniqueWordsInSong);
                    updateUniqueWords(words);

                    SongData songData = new SongData(title,uniqueWordsInSong,title+"-"+artist,id,tfVals,new HashMap<String,Double>());

                    dataObjs.put(title+"-"+artist, songData);
                    idToSong.put(id,title+"-"+artist);
                    id++;
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }
        }

        createWordObjects(id);
        ArrayList<String> vectorId = createVectorId();
        vectorIdDocuments(vectorId,id);
        addTfIdf(vectorId);

        storePageDataObjects();
        return id;
    }

//  In the PageData object it creates a HashMap<String,Double> that has a word as a key and a value as the tfidf.
    private void addTfIdf(ArrayList<String> vectorWordMap){
        for (Map.Entry<String, SongData> set : dataObjs.entrySet()) {
            Map<String,Double> tfIdfMap = set.getValue().getTfIdfVals();
            for (String word:vectorWordMap){
                double tfVal = 0.0;
                double idfVal = 0.0;
                if (set.getValue().getTfVals().containsKey(word)){
                    tfVal = set.getValue().getTfVals().get(word);
//                    System.out.println(tfVal);
                }
                if (wordObjs.containsKey(word)){
                    idfVal = wordObjs.get(word).getIdfVal();
//                    System.out.println(idfVal);
                }
                double tfIdf = MatMult.logBasex(1+tfVal,2)*idfVal;

//                System.out.println(tfIdf);
                tfIdfMap.put(word,tfIdf);
            }
        }
    }

//   vectorIdDocuments takes vectorWordMap and numOfPages. Then it will compute the vector for each id/page and store it (id-vector-list.txt) in a folder called id.
    private void vectorIdDocuments(ArrayList<String> vectorWordMap, int totalPages){
        ArrayList<List<Double>> vectorList = new ArrayList<List<Double>>();
        int idSong = 0;
        while (idSong < totalPages){
            String url = idToSong.get(idSong);
            List<Double> innerList = new ArrayList<Double>();
            for (String word:vectorWordMap) {
                double tfVal = 0.0;
                double idfVal = 0.0;
                if (dataObjs.get(url).getTfVals().containsKey(word)) {
                    tfVal = dataObjs.get(url).getTfVals().get(word);
                }
                if (wordObjs.containsKey(word)) {
                    idfVal = wordObjs.get(word).getIdfVal();
                }
                double tfIdf = MatMult.logBasex(1 + tfVal, 2) * idfVal;
                innerList.add(tfIdf);
            }
            vectorList.add(innerList);
            idSong++;
        }
        fileModifier.addVectorListToFile(vectorList);
    }
/*
    vectorId is used to create a file called id-vector.txt in a folder called id.
    This is done to make sure that the space vector have the same order the Search and Crawler.
    it returns a list called vectorWordList
*/
    private ArrayList<String> createVectorId(){
        ArrayList<String> vectorWordList = new ArrayList<>();
        Map<String,Integer> vectorWordMap = new HashMap<String,Integer>();
        for (Map.Entry<String,Integer> set : uniqueWords.entrySet()) {
            vectorWordList.add(set.getKey());
        }
        for (int i=0;i<vectorWordList.size();i++){
            vectorWordMap.put(vectorWordList.get(i),i);
        }
        fileModifier.addVectorIdMap(vectorWordMap);
        return vectorWordList;
    }

// Stores all PageDataObjects in the PageDataObjects according to it's unique id.
    private void storePageDataObjects(){
        for (Map.Entry<String, SongData> set : dataObjs.entrySet()) {
            fileModifier.addPageDataToFile(set.getValue().getId()+".txt", set.getValue());
        }
    }

//   Create Word objects that store idf values.
    private void createWordObjects(int totalPages){
        for (Map.Entry<String,Integer> set : uniqueWords.entrySet()) {
            String word = set.getKey();
            double idf = 0;
            idf = MatMult.logBasex((double) totalPages/(1+set.getValue()),2);
            if (totalPages == set.getValue()){
                idf = 1;
            }
//            System.out.println(idf);
//            System.out.println(word);
//            System.out.println(set.getValue());
            Word wordObj = new Word(word,idf);
            wordObjs.put(word,wordObj);
            fileModifier.addWordToFile(word+".txt",wordObj);
        }
    }

// Returns a Hashmap<String,Integer> that stores the amount of times a word is referenced in a html page.
    private HashMap<String,Integer> createUniqueWordsInSong(ArrayList<String> words){
        HashMap<String,Integer> uniqueWordsInPage = new HashMap<String,Integer>();
        for (String word:words){
            if (uniqueWordsInPage.containsKey(word)){
                uniqueWordsInPage.put(word,uniqueWordsInPage.get(word)+1);
            }
            else{
                uniqueWordsInPage.put(word,1);
            }
        }
        return uniqueWordsInPage;
    }

//    Update every Unique word using the uniqueWords Map.
    private void updateUniqueWords(ArrayList<String> words){
        HashMap<String,Integer> uniqueWordsInPage = new HashMap<String,Integer>();
        for (String word:words){
            if (uniqueWordsInPage.containsKey(word)){
                uniqueWordsInPage.put(word,uniqueWordsInPage.get(word)+1);
            }
            else{
                uniqueWordsInPage.put(word,1);
                if (uniqueWords.containsKey(word)){
                    uniqueWords.put(word,uniqueWords.get(word)+1);
                }
                else{
                    uniqueWords.put(word,1);
                }
            }
        }
    }


    //Calculate tf value
    private HashMap<String,Double> createTfVals(ArrayList<String> words,HashMap<String,Integer> uniqueWordsInPage){
        HashMap<String,Double> tfVals = new HashMap<String,Double>();
        int totalWordCount = words.size();
        for (Map.Entry<String,Integer> set : uniqueWordsInPage.entrySet()) {
            double tf = (double)set.getValue()/totalWordCount;
            tfVals.put(set.getKey(),tf);
        }

        return tfVals;
    }
}
