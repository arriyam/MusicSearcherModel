package Tools;

import DataObjects.SongData;
import DataObjects.Word;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileModifier {
    private String parentDir;

    public FileModifier() {
        parentDir = System.getProperty("user.dir");
    }

    public String getParentDir(){
        return parentDir;
    }

    private boolean createFolder(String directoryName) {
        String path = parentDir + File.separator + directoryName;
        File file = new File(path);
        if (file.mkdir()) {
            return true;
        }
        return false;
    }

    private boolean deleteFolder(String directoryName) {
        String path = parentDir + File.separator + directoryName;
        File file = new File(path);
        String[] entries = file.list();
        if (entries != null) {
            for (String s : entries) {
                File currentFile = new File(file.getPath(), s);
                currentFile.delete();
            }
            if (file.delete()) {
                return true;
            }
        }
        return false;
    }

    public SongData getPageDataFromFile(String fileName) {
        String path = parentDir + File.separator + "PageDataObjects" + File.separator + fileName;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
            SongData page = (SongData) in.readObject();
            in.close();
            return page;
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Object's class does not match");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot read from file");
        }
        return null;
    }

    public boolean addPageDataToFile(String fileName, SongData page) {
        String path = parentDir + File.separator + "PageDataObjects" + File.separator + fileName;
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
            out.writeObject((SongData) page);
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot write to file");
        }
        return false;
    }
    public Word getWordFromFile(String fileName) {
        String path = parentDir + File.separator + "WordObjects" + File.separator + fileName;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
            Word page = (Word) in.readObject();
            in.close();
            return page;
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Object's class does not match");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot read from file");
        }
        return null;
    }


    public boolean addWordToFile(String fileName, Word word) {
        String path = parentDir + File.separator + "WordObjects" + File.separator + fileName;
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
            out.writeObject((Word) word);
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot write to file");
        }
        return false;
    }


    public boolean addVectorListToFile(List<List<Double>> vectorList) {
        String path = parentDir + File.separator + "id" + File.separator + "id-vector-list.txt";
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
            out.writeObject(vectorList);
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot write to file");
        }
        return false;
    }

    public List<List<Double>> getVectorListToFile() {
        String path = parentDir + File.separator + "id" + File.separator + "id-vector-list.txt";
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
            List<List<Double>> vectorList = (ArrayList<List<Double>>) in.readObject();
            in.close();
            return vectorList;
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Object's class does not match");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot read from file");
        }
        return null;
    }

    public boolean addVectorIdMap(Map<String,Integer> vectorWordMap){
        String path = parentDir + File.separator + "id" + File.separator + "id-vector.txt";
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
            out.writeObject(vectorWordMap);
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot write to file");
        }
        return false;
    }

    public Map<String,Integer> getVectorIdMap() {
        String path = parentDir + File.separator + "id" + File.separator + "id-vector.txt";
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
            Map<String,Integer> vectorIdMap = (HashMap<String,Integer>) in.readObject();
            in.close();
            return vectorIdMap;
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Object's class does not match");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot read from file");
        }
        return null;
    }

    public boolean addIdHashMapToFile(Map<String,Integer> urlsVisited) {
        String path = parentDir + File.separator + "id" + File.separator + "id-url-map.txt";
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
            out.writeObject(urlsVisited);
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot write to file");
        }
        return false;
    }


    public Map<String,Integer> getIdHashMapToFile() {
        String path = parentDir + File.separator + "id" + File.separator + "id-url-map.txt";
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
            Map<String,Integer> urlsVisited = (HashMap<String, Integer>) in.readObject();
            in.close();
            return urlsVisited;
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Object's class does not match");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot read from file");
        }
        return null;
    }

    public boolean addUrlHashMapToFile(Map<Integer,String> idToUrl) {
        String path = parentDir + File.separator + "id" + File.separator + "url-id-map.txt";
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
            out.writeObject(idToUrl);
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot write to file");
        }
        return false;
    }

    public Map<Integer,String> getUrlHashMapToFile() {
        String path = parentDir + File.separator + "id" + File.separator + "url-id-map.txt";
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
            Map<Integer,String> idToUrl = (HashMap<Integer,String>) in.readObject();
            in.close();
            return idToUrl;
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Object's class does not match");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot read from file");
        }
        return null;
    }

    public String idToUrl(int id){
        Map<Integer,String> idToUrl = getUrlHashMapToFile();
        if (idToUrl.containsKey(id)){
            return idToUrl.get(id);
        }
        return "";
    }

    public int urlToId(String url){
        Map<String,Integer> urlsVisited = getIdHashMapToFile();
        if (urlsVisited.containsKey(url)){
            return urlsVisited.get(url);
        }
        return -1;
    }

    public void initialize() {
        deleteFolder("id");
        deleteFolder("PageDataObjects");
        deleteFolder("WordObjects");
        createFolder("id");
        createFolder("PageDataObjects");
        createFolder("WordObjects");
    }

}

