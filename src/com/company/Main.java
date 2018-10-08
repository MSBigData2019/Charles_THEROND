package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Mapper wordcount= new Mapper();
        wordcount.wordcount(args[0].toString());
        Integer N=Integer.parseInt(args[1]);
        Integer sort = Integer.parseInt(args[2]);
        wordcount.display(N,sort);
        /**
        // Question 1 à 3
        System.out.println("input.txt");
        System.out.println("=============================");
        wordcount.wordcount("input.txt");
        wordcount.display(15,0);
        System.out.println("=============================");
        // Question 4
        System.out.println("forestier mayotte");
        System.out.println("=============================");
        wordcount.wordcount("forestier_mayotte.txt");
        wordcount.display(15,0);
        System.out.println("=============================");
        // Question 7
        System.out.println("deontologie police nationale");
        System.out.println("=============================");
        wordcount.wordcount("deontologie_police_nationale.txt");
        wordcount.display(15,0);
        System.out.println("=============================");
        // Question 8
        System.out.println("dommaine public fluvial");
        System.out.println("=============================");
        wordcount.wordcount("domaine_public_fluvial.txt");
        wordcount.display(15,0);
        System.out.println("=============================");
        // Question 9
        System.out.println("sante public");
        System.out.println("=============================");
        wordcount.wordcount("sante_publique.txt");
        wordcount.display(15,0);
        System.out.println("=============================");
        // Question 11
        System.out.println("CC-MAIN-20170322212949-00140-ip-10-233-31-227.ec2.internal.warc.wet");
        System.out.println("=============================");
        wordcount.wordcount("CC-MAIN-20170322212949-00140-ip-10-233-31-227.ec2.internal.warc.wet");
        wordcount.display(15,0);
        System.out.println("=============================");
        */
    }
}
