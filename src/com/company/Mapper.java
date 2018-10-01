package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Mapper {
    String content;
    HashMap<String, Integer> mapped = new HashMap<String, Integer>();

    public Mapper(String input){
        System.out.println(input);
        content = input;

    }

    // Question 1 comptage séquentiel pur
    public void wordcount() {
        String[] montexttab= content.split(" ");
        mapped = new HashMap<String, Integer>();
        for (String word : montexttab){
            Integer value = mapped.get(word);
            if(value==null){
                mapped.put(word,1);
            }else{
                mapped.put(word, value + 1);
            }
        }
    }

    // Question 2 sorting par valeur
    public void sortingWordcount(){
        HashMap<Integer, ArrayList<String>> transitionMap = new HashMap<Integer, ArrayList<String>>();
        ArrayList<Integer> occurences = new ArrayList<Integer>();
        for (String name: mapped.keySet()){
            String word =name;
            Integer occurence = mapped.get(name);
            if(transitionMap.containsKey(occurence)){
                ArrayList<String> listWord= transitionMap.get(occurence);
                listWord.add(word);
                transitionMap.put(occurence,listWord);
            }else{
                ArrayList<String> init=new ArrayList<String>();
                init.add(word);
                occurences.add(occurence);
                transitionMap.put(occurence, init);
            }
        }
        Collections.sort(occurences,Collections.reverseOrder());
        System.out.println("-----------------------------");
        Integer n=0;
        for (Integer i: occurences){
            ArrayList<String> listWord = transitionMap.get(i);
            // Question 3 sorting par valeur et texte
            Collections.sort(listWord); // commenter cette ligne pour switcher entre question 2 et 3
            for (String txt:listWord){
                if (n<50){ // Question 10 commenter cette ligne pour obtenir uniquement les temps de calcul
                    System.out.println(txt+"  "+i.toString());
                }
                n++;
            }

        }
        System.out.println("-----------------------------");


    }

    // reading file and transforming into string.
    public void setContent(String text) {

        FileInputStream fis = null;
        content="";
        try {
            fis = new FileInputStream(text);
            byte[] buffer = new byte[1];
            StringBuilder sb = new StringBuilder();
            while (fis.read(buffer) != -1) {
                sb.append(new String(buffer));
                buffer = new byte[1];
            }
            fis.close();
            content = sb.toString().replaceAll("\\n"," " );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public HashMap<String, Integer> getMapped() {
        return mapped;
    }
}
