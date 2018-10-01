package com.company;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Mapper wordcount= new Mapper("");
        // Question 1 à 3
        System.out.println("input.txt");
        System.out.println("=============================");
        wordcount.setContent("input.txt");
        long startWordcount = System.currentTimeMillis();
        wordcount.wordcount();
        long endWordcount   = System.currentTimeMillis();
        long totalWordcount = endWordcount - startWordcount;
        long startSorting = System.currentTimeMillis();
        wordcount.sortingWordcount();
        long endSorting   = System.currentTimeMillis();
        long totalSorting = (endSorting - startSorting);
        System.out.println("-----------------------------");
        System.out.println("Le temps du wordcount est de "+ String.valueOf(totalWordcount));
        System.out.println("Le temps du sorting est de "+ String.valueOf(totalSorting));
        System.out.println("-----------------------------");
        System.out.println("=============================");

        // Question 4
        System.out.println("forestier mayotte");
        System.out.println("=============================");
        wordcount.setContent("forestier_mayotte.txt");
        startWordcount = System.currentTimeMillis();
        wordcount.wordcount();
        endWordcount   = System.currentTimeMillis();
        totalWordcount = (endWordcount - startWordcount);
        startSorting = System.currentTimeMillis();
        wordcount.sortingWordcount();
        endSorting   = System.currentTimeMillis();
        totalSorting = (endSorting - startSorting);
        System.out.println("-----------------------------");
        System.out.println("Le temps du wordcount est de "+ String.valueOf(totalWordcount));
        System.out.println("Le temps du sorting est de "+ String.valueOf(totalSorting));
        System.out.println("-----------------------------");
        System.out.println("=============================");

        // Question 7
        System.out.println("deontologie police nationale");
        System.out.println("=============================");
        wordcount.setContent("deontologie_police_nationale.txt");
        startWordcount = System.currentTimeMillis();
        wordcount.wordcount();
        endWordcount   = System.currentTimeMillis();
        totalWordcount = (endWordcount - startWordcount);
        startSorting = System.currentTimeMillis();
        wordcount.sortingWordcount();
        endSorting   = System.currentTimeMillis();
        totalSorting = (endSorting - startSorting);
        System.out.println("-----------------------------");
        System.out.println("Le temps du wordcount est de "+ String.valueOf(totalWordcount));
        System.out.println("Le temps du sorting est de "+ String.valueOf(totalSorting));
        System.out.println("-----------------------------");
        System.out.println("=============================");
        /**
         * 1 de
         * 2 la
         * 3 police
         * 4 et
         * 5 des
         */
        // Question 8
        System.out.println("dommaine public fluvial");
        System.out.println("=============================");
        wordcount.setContent("domaine_public_fluvial.txt");
        startWordcount = System.currentTimeMillis();
        wordcount.wordcount();
        endWordcount   = System.currentTimeMillis();
        totalWordcount = (endWordcount - startWordcount);
        startSorting = System.currentTimeMillis();
        wordcount.sortingWordcount();
        endSorting   = System.currentTimeMillis();
        totalSorting = (endSorting - startSorting);
        System.out.println("-----------------------------");
        System.out.println("Le temps du wordcount est de "+ String.valueOf(totalWordcount));
        System.out.println("Le temps du sorting est de "+ String.valueOf(totalSorting));
        System.out.println("-----------------------------");
        System.out.println("=============================");
        /**
         * 1 le
         * 2 du
         * 3 la
         * 4 et
         * 5 des
         */
        // Question 9
        System.out.println("sante public");
        System.out.println("=============================");
        wordcount.setContent("sante_publique.txt");
        startWordcount = System.currentTimeMillis();
        wordcount.wordcount();
        endWordcount   = System.currentTimeMillis();
        totalWordcount = (endWordcount - startWordcount);
        startSorting = System.currentTimeMillis();
        wordcount.sortingWordcount();
        endSorting   = System.currentTimeMillis();
        totalSorting = (endSorting - startSorting);
        System.out.println("-----------------------------");
        System.out.println("Le temps du wordcount est de "+ String.valueOf(totalWordcount));
        System.out.println("Le temps du sorting est de "+ String.valueOf(totalSorting));
        System.out.println("-----------------------------");
        System.out.println("=============================");
        /**
         * 1 de
         * 2 la
         * 3 des
         * 4 et
         * 5 les
         */
        // Question 11
        System.out.println("CC-MAIN-20170322212949-00140-ip-10-233-31-227.ec2.internal.warc.wet");
        System.out.println("=============================");
        wordcount.setContent("CC-MAIN-20170322212949-00140-ip-10-233-31-227.ec2.internal.warc.wet");
        startWordcount = System.currentTimeMillis();
        wordcount.wordcount();
        endWordcount   = System.currentTimeMillis();
        totalWordcount = (endWordcount - startWordcount);
        startSorting = System.currentTimeMillis();
        wordcount.sortingWordcount();
        endSorting   = System.currentTimeMillis();
        totalSorting = (endSorting - startSorting);
        System.out.println("-----------------------------");
        System.out.println("Le temps du wordcount est de "+ String.valueOf(totalWordcount));
        System.out.println("Le temps du sorting est de "+ String.valueOf(totalSorting));
        System.out.println("-----------------------------");
        System.out.println("=============================");
        /**
         * 1 de
         * 2 la
         * 3 des
         * 4 et
         * 5 les
         */
    }
}
