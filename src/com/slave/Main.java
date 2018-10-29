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

            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture de " + text);
                System.exit(1);
            }





        }
        if (args[0].equals("1")) {
            //ssh charlestherond@localhost java -jar /tmp/ctherond/JAR/map.jar 1 Car /tmp/ctherond/mapResult/SM1.txt /tmp/ctherond/mapResult/maps/UM1.txt /tmp/ctherond/mapResult/maps/UM2.txt
            FileWriter writer = new FileWriter(args[2]);
            for(int i= 3; i < args.length;i++){
                System.out.println(args[i]);
                HashMap<String, Integer> mapped = new HashMap<String, Integer>();
                List<String> lines = null;
                try {
                    lines = Files.readAllLines(Paths.get(args[i]), Charset.forName("UTF-8"));


                    for (String line : lines) {
                        line.replaceAll("\\n", " ");
                        String[] montexttab = line.split(" ");
                        for (String word : montexttab) {
                            if (word.equals(args[1])) {
                                writer.write(word + " 1");
                                writer.write(System.lineSeparator());
                                mapped.put(word, 1);
                            }

                        }
                    }
                    System.out.println(mapped.keySet());

                } catch (IOException e) {
//                    System.out.println("Erreur lors de la lecture de " + args[i]);
                    System.out.println(e);
                    System.exit(1);
                }



            }

            writer.close();

        }
        if (args[0].equals("2")) {
            FileWriter writer = new FileWriter("/tmp/ctherond/reduce/"+args[3]);
                List<String> lines = null;
                try {
                    lines = Files.readAllLines(Paths.get(args[2]), Charset.forName("UTF-8"));

                    Integer sum =0;
                    for (String line : lines) {
                        line.replaceAll("\\n", " ");
                        String[] montexttab = line.split(" ");
                        if (montexttab[0].equals(args[1])){
                            sum+=Integer.parseInt(montexttab[1]);
                        }
                    }
                    writer.write(args[1] +" "+sum);
                    System.out.println(args[1] +" "+sum);
                    System.out.println(args[3]);

                } catch (IOException e) {
//                    System.out.println("Erreur lors de la lecture de " + args[i]);
                    System.out.println(e);
                    System.exit(1);
                }





            writer.close();
        }
    }


}

