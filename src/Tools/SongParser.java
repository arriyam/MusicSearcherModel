package Tools;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

public class SongParser {

    private String lyric;
    private String title;
    private String artist;

    public SongParser(String songData,String fileName){
         lyric = songData;
         String[] fileNameArray = fileName.split("-");
        title = fileNameArray[0];
        artist = fileNameArray[1].substring(0,fileNameArray[1].length()-4);
    }

    public String parseTitle(){
        return title;
    }

    public ArrayList<String> parseWords(){
        ArrayList<String> words = new ArrayList<String>();

        lyric = lyric.replace("\n"," ");
        lyric = lyric.replace("\""," ");
        lyric = lyric.replace("(","");
        lyric = lyric.replace(")","");
        lyric = lyric.replace("!","");
        lyric = lyric.replace(".","");
        lyric = lyric.replace(",","");
        lyric = lyric.replace("?","");
        lyric = lyric.replace("'","");
        lyric = lyric.strip();
        String[] lyricArray = lyric.split(" ");

        for (String word:lyricArray){
            words.add(word.toLowerCase(Locale.ROOT));
        }

        return words;
    }

    public String parseArtist(){
        return artist;
    }

}
