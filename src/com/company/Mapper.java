package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Mapper {
    String content;
    HashMap<String, Integer> mapped = new HashMap<String, Integer>();
    ArrayList<Integer> occurences = new ArrayList<Integer>();
    HashMap<Integer, ArrayList<String>> occurenceMap = new HashMap<Integer, ArrayList<String>>();

    public Mapper(){
    }

    // Question 1 comptage séquentiel pur
    public void wordcount(String text) {
        mapped = new HashMap<String, Integer>();
        List<String> lines = null;

        try {
            System.out.println("-----------------------------");
            long startCharge = System.currentTimeMillis();
            lines = Files.readAllLines(Paths.get(text), Charset.forName("UTF-8"));
            long endCharge = System.currentTimeMillis();
            long totalCharge = (endCharge - startCharge);
            System.out.println("Le temps du chargement de fichier est de "+ String.valueOf(totalCharge));

        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture de " + text);
            System.exit(1);
        }
        System.out.println("-----------------------------");
        long startWord = System.currentTimeMillis();
        for (String line : lines) {
            line.replaceAll("\\n"," " );
            String[] montexttab= line.split(" ");
            for (String word : montexttab){
                Integer value = mapped.get(word);
                if(value==null){
                    mapped.put(word,1);
                }else{
                    mapped.put(word, value + 1);
                }
            }
        }
        long endWord= System.currentTimeMillis();
        long totalWord = (endWord - startWord);
        System.out.println("Le temps du wordcount est de "+ String.valueOf(totalWord));
        System.out.println("-----------------------------");

    }

    // Question 2 sorting par valeur
    public void occurencesSort( Integer sorting){
        occurenceMap = new HashMap<Integer, ArrayList<String>>();
        occurences = new ArrayList<Integer>();
        for (String name: mapped.keySet()){
            String word =name;
            Integer occurence = mapped.get(name);
            if(occurenceMap.containsKey(occurence)){
                ArrayList<String> listWord= occurenceMap.get(occurence);
                listWord.add(word);

                occurenceMap.put(occurence,listWord);
            }else{
                ArrayList<String> init=new ArrayList<String>();
                init.add(word);
                occurences.add(occurence);
                occurenceMap.put(occurence, init);
            }
        }
        Collections.sort(occurences,Collections.reverseOrder());
        if( sorting == 0){
            for(Integer i : occurences){
                ArrayList<String> listWord= occurenceMap.get(i);
                Collections.sort(listWord);
                occurenceMap.put(i,listWord);
            }

        }


    }




    public void display(int N, Integer sorting){
        long startSorting = System.currentTimeMillis();
        this.occurencesSort(sorting);
        long endSorting   = System.currentTimeMillis();
        long totalSorting = (endSorting - startSorting);
        System.out.println("Le temps du sorting est de "+ String.valueOf(totalSorting));
        System.out.println("-----------------------------");

        System.out.println("Les "+ String.valueOf(N)+" premiers mot sont:");
        long startDisplay = System.currentTimeMillis();

            int n=0;
            for( Integer i: occurences){
                ArrayList<String> listWord = occurenceMap.get(i);

                for(String word : listWord){
                    System.out.println(word+" "+String.valueOf(i));
                    n++;
                }
                if(n>=N){
                    break;
                }

            }
        long endDisplay   = System.currentTimeMillis();
        long totalDisplay = (endDisplay - startDisplay);
        System.out.println("-----------------------------");
        System.out.println("Le temps du display est de "+ String.valueOf(totalDisplay));
        System.out.println("-----------------------------");





    }



    public HashMap<String, Integer> getMapped() {
        return mapped;
    }


}