package Model;

import Blueprint.ProjectTester;
import Blueprint.SearchResult;
import Tools.FileModifier;

import java.util.List;
import java.util.Map;

public class WebCrawler implements ProjectTester {
    private FileModifier fileModifier;
    private Crawler crawler;
    private Search search;

    public WebCrawler(){
        fileModifier = new FileModifier();
        crawler = new Crawler();
        search = new Search();
    }

    public Map<String,Integer> getIdHashMap() {return fileModifier.getIdHashMapToFile();}
    public void initialize() { fileModifier.initialize(); }
    public void crawl(String seedURL) {crawler.crawl(seedURL);}
    public double getIDF(String word) { return search.getIDF(word); }
    public double getTF(String url, String word) { return search.getTF(url,word); }
    public double getTFIDF(String url, String word) { return search.getTFIDF(url,word);}
    public List<SearchResult> search(String query, boolean boost, int X) {
        return search.search(query,X);
    }
}
