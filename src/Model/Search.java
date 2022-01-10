package Model;

import Blueprint.SearchResult;
import DataObjects.SongData;
import DataObjects.Word;
import Tools.FileModifier;
import Tools.MatMult;

import java.util.*;

public class Search {

    private FileModifier fileModifier;

    public Search(){
        fileModifier = new FileModifier();
    }

//    search process the search inquiry and return x number of results back to users in a List<SearchResult>.
    public List<SearchResult> search(String query, int numResults) {
        Map<String,Integer> vectorIdMap = fileModifier.getVectorIdMap();
        List<List<Double>> vectorList = fileModifier.getVectorListToFile();
//        System.out.println(vectorIdMap);
//        System.out.println(vectorList);
        List<String> queryList = createQueryLower(query);
        int queryLength = queryList.size();
        Map<String,Integer> QueryMapCount = createQueryMapCount(queryList);
        queryList = removeDuplicateInList(queryList);
        List<Double> queryVector = createQueryVector(queryList,QueryMapCount,queryLength);
        vectorList = simplifyVectorList(queryList,vectorIdMap,vectorList);
        List<Double> cosSimilarity = createCosineSimilarity(queryVector,vectorList);
        TreeSet<Result> results = addToTreeSet(cosSimilarity,numResults);
        return createSearchResultList(results,numResults);
    }

// Takes the query from user and goes through all the words and makes sure it is converted to all lower cases. It returns a List<String> or lower case words
    private List<String> createQueryLower(String queryStr){
        queryStr = queryStr.replace(",","");
        String[] query = queryStr.trim().split(" ");
        List<String> queryList = new ArrayList<String>();
        for (String word:query){
            queryList.add(word);
        }
        int phrase = 0;
        while (phrase < queryList.size()){
            String lowerWord = queryList.get(phrase).toLowerCase(Locale.ROOT);
            queryList.set(phrase,lowerWord);
            if (lowerWord.isBlank()){
                queryList.remove(phrase);
            }
            else{
                phrase++;
            }
        }
        return queryList;
    }
// Grabs the queryList and creates a query map and removes duplicate elements in the queryList list.
    private Map<String,Integer> createQueryMapCount(List<String> queryList){
        Map<String,Integer> queryMapCount = new HashMap<String,Integer>();
        for (String word:queryList){
            if (queryMapCount.containsKey(word)){
                queryMapCount.put(word,queryMapCount.get(word)+1);
            }
            else{
                queryMapCount.put(word,1);
            }
        }
        return queryMapCount;
    }

    // Grabs the queryList and removes duplicates from the list.
    private List<String> removeDuplicateInList(List<String> queryList){
        HashSet<String> wordSet = new HashSet<>();
        int i = 0;
        while (i < queryList.size()){
            if (wordSet.contains(queryList.get(i))){
                queryList.remove(i);
            }
            else{
                wordSet.add(queryList.get(i));
                i++;
            }
        }
        return queryList;
    }

//  Creates a query vector with the queryList,queryMapCount and queryLength.
    private List<Double> createQueryVector(List<String> queryList,Map<String,Integer> queryMapCount,int queryLength){
        List<Double> queryVector = new ArrayList<Double>();
        double idf,tf,tfIdf;
        int i =0;
        while (i<queryList.size()){
            String query = queryList.get(i);
            idf = getIDF(query);
            if (idf > 0){
                tf = (double)queryMapCount.get(query)/queryLength;
                tfIdf = MatMult.logBasex(1+tf,2)*idf;
                queryVector.add(tfIdf);
                i++;
            }
            else{
                queryList.remove(i);
            }
        }
        return queryVector;
    }

// Creates a new vector list removing all tf-idf word values if is not present in the query the user asked.
    private List<List<Double>> simplifyVectorList(List<String> queryList,Map<String,Integer> vectorIdMap,List<List<Double>> vectorList){
        List<List<Double>> newVectorList = new ArrayList<List<Double>>();
        for (int i=0;i<vectorList.size();i++){
            List<Double> innerList = new ArrayList<Double>();
            for (String query:queryList){
                if (vectorIdMap.containsKey(query)){
                    innerList.add(vectorList.get(i).get(vectorIdMap.get(query)));
                }
            }
            newVectorList.add(innerList);
        }
        return newVectorList;
    }

//Computes the cosine similarity for each page.
    private List<Double> createCosineSimilarity(List<Double> queryVector,List<List<Double>> vectorList){
        List<Double> cosSimilarity = new ArrayList<Double>();
        for (int i = 0;i<vectorList.size();i++){
            double numerator = 0.0;
            double leftDenominator = 0.0;
            double rightDenominator = 0.0;
            double cosSimilarityValue;
            for (int b = 0;b < queryVector.size();b++){
                numerator += vectorList.get(i).get(b) * queryVector.get(b);
                leftDenominator += Math.pow(queryVector.get(b),2);
                rightDenominator += Math.pow(vectorList.get(i).get(b),2);
            }
            if ((leftDenominator == 0) || (rightDenominator==0) || (numerator == 0)){
                cosSimilarityValue = 0.0;
            }
            else{
                cosSimilarityValue = numerator/(Math.pow(leftDenominator,0.5)*Math.pow(rightDenominator,0.5));
            }
            cosSimilarity.add(cosSimilarityValue);
        }
        return cosSimilarity;
    }



//    comments...
    private TreeSet<Result> addToTreeSet(List<Double> cosSimilarity, int numResults){
        TreeSet<Result> results = new TreeSet<Result>();
        for (int id = 0; id < cosSimilarity.size(); id++) {
            SongData page = fileModifier.getPageDataFromFile(id+".txt");
            Result current = new Result(page.getTitle(), page.getUrl(),cosSimilarity.get(id));
            results = addToSet(current,results,numResults);
        }
        return results;
    }

    //Call this function when adding an item to the set.
    private TreeSet<Result> addToSet(Result result, TreeSet<Result> results, int size){
        if(results.contains(result)){
            return results;
        }
        if (results.size() < size){
            results.add(result);
        }
        else if(result.compareTo(results.last()) == -1){
            results.add(result);
            results.remove(results.last());
        }
        return results;
    }

//    Adds every object in the treeSet into an array and casts each element into the searchResult type.
    private List<SearchResult> createSearchResultList(TreeSet<Result> results,int numResults){
        List<SearchResult> output = new ArrayList<SearchResult>(numResults);
        for (int i = 0; i < numResults; i++) {
            Result current = results.first();
            results.remove(current);
            output.add((SearchResult)current);
        }
        return output;
    }




// Takes a word and grabs the specific object from a file and returns the IDF.
    public double getIDF(String word) {
        Word wordObj = fileModifier.getWordFromFile(word+".txt");
        if (wordObj == null){
            return 0.0;
        }
        double idf = wordObj.getIdfVal();
        return idf;
    }

// Takes an url and grabs the specific object from a file. The object contains a hashmap which contains words as keys and TF as values.
    public double getTF(String url, String word) {
        int id = fileModifier.urlToId(url);
        SongData page = fileModifier.getPageDataFromFile(id+".txt");
        if (page==null){
            return 0.0;
        }
        return page.getTfVals().getOrDefault(word, 0.0);
    }

    // Takes an url and grabs the specific object from a file. The object contains a hashmap which contains words as keys and TFIDF as values.
    public double getTFIDF(String url, String word) {
        int id = fileModifier.urlToId(url);
        SongData page = fileModifier.getPageDataFromFile(id+".txt");
        if (page==null){
            return 0.0;
        }
        return page.getTfIdfVals().getOrDefault(word, 0.0);
    }
}