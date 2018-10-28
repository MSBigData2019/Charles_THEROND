package com.slave;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {


        if (args[0].equals("0")) {
            String numberOnly = args[1].replaceAll("[^0-9]", "");
            String outputName = "UM" + String.valueOf(numberOnly) + ".txt";
            System.out.println(outputName);
            String text = "/tmp/ctherond/split/" + args[1];
            HashMap<String, Integer> mapped = new HashMap<String, Integer>();
            List<String> lines = null;
            try {
                lines = Files.readAllLines(Paths.get(text), Charset.forName("UTF-8"));

            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture de " + text);
                System.exit(1);
            }


            FileWriter writer = new FileWriter("/tmp/ctherond/mapResult/" + outputName);

            for (String line : lines) {
                line.replaceAll("\\n", " ");
                String[] montexttab = line.split(" ");
                for (String word : montexttab) {
                    writer.write(word + " 1");
                    writer.write(System.lineSeparator());
                    mapped.put(word, 1);

                }
            }
            System.out.println(mapped.keySet());
            writer.close();


        }
        if (args[0].equals("1")) {

        }
    }


}

