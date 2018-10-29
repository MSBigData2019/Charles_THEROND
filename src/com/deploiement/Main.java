package com.deploiement;

import com.master.CopyThread;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import sun.awt.AWTIcon32_security_icon_yellow16_png;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static jdk.nashorn.internal.objects.ArrayBufferView.length;


public class Main {

    public static HashMap<String,String> stage = new HashMap<>();
    public static HashMap<String,String> referenceFichier = new HashMap<>();
    public static HashMap<String,ArrayList<String>> cleMachine=new HashMap<>();
    private static HashMap<String, ArrayList<String>> machineRN;


    public static void main(String[] args){
        // test de la connexion
        try {
            BlockingDeque<HashMap<String,ArrayList<String>>> queue = new LinkedBlockingDeque<>();
            List<String> command= new ArrayList<String>();
            List<String> lines = null;
            HashMap<String,Process> processs=new HashMap<>();
            HashMap<String,ArrayList<String>> result=new HashMap<>();
            lines = Files.readAllLines(Paths.get("configAll/config.txt"), Charset.forName("UTF-8"));
            for (String line : lines) {
                System.out.println("Test la connexion à "+line);
                command= new ArrayList<String>();
                command.add("ssh");
                command.add(line);
                command.add("whereis");
                command.add("java");
                ProcessBuilder builder = new ProcessBuilder(command);
                try {
                    Process p = builder.start();
                    Thread mythread = new TestSSH(p,queue,line);
                    mythread.start();
                    processs.put(line,p);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for(String line:lines){
                HashMap<String,ArrayList<String>> r=queue.poll(5,TimeUnit.SECONDS);
                result.put(r.keySet().toArray()[0].toString(),r.get(r.keySet().toArray()[0]));

            }

            for (String line:result.keySet()){
                System.out.println(line);
                System.out.println(result.get(line));
                if(result.get(line).toArray()==null){
                    Process px=processs.get(line);
                    px.destroy();
                    System.out.println("TIMEOUT");
                    System.out.println("Nous n'avons pas execute :");
                    System.out.println(command);
                }
                if (!result.get(line).toArray()[0].equals("Host key verification failed.")){
                    System.out.println("Copie des fichiers autorisée sur "+line);
                    stage.put(line,"step1");
                }else{
                    System.out.println("Echec de connexion");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

//        // Repartition du split
//        List<String> listeFichiers = null;
//        try {
//            listeFichiers = Files.readAllLines(Paths.get("configAll/fileList.txt"), Charset.forName("UTF-8"));
//            for (String lf : listeFichiers) {
//                System.out.println(lf);
//                List<String> fichier = null;
//                try {
//                    fichier = Files.readAllLines(Paths.get(lf), Charset.forName("UTF-8"));
//                    // Supposons que l'ordre des mots est important
//                    // on commence par determiner ou on va spliter notre fichier
//                    float numWordForSplit=0;
//                    for (String line : fichier) {
//                        numWordForSplit+=line.split(" ").length;
//                    }
//                    numWordForSplit=(numWordForSplit / stage.size());
//                    if ( (numWordForSplit/stage.size() -(long)numWordForSplit/stage.size()) !=0.0){
//                        numWordForSplit++;
//                    }
//                    numWordForSplit=(long)numWordForSplit;
//                    // On split le fichier
//                    Integer nWord=0;
//                    String filename="1split"+lf;
//                    for (String line : fichier) {
//                        Writer writer = null;
//
//                        try {
//                            writer = new BufferedWriter(new OutputStreamWriter(
//                                    new FileOutputStream(filename), "utf-8"));
//                            writer.write(line);
//
//
//                        } catch (IOException ex) {
//                            // Report
//                        } finally {
//                            try {writer.close();} catch (Exception ex) {/*ignore*/}
//                        }
//
//                    }
//
//                }catch (IOException e) {
//
//                    //e.printStackTrace();
//                }
//
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        //Executions
        Boolean condition= Boolean.TRUE;
        Integer maxIteration= 0;
        try{
            BlockingDeque<HashMap<String,ArrayList<String>>> queue = new LinkedBlockingDeque<>();
            HashMap<String,Process> processs=new HashMap<>();
            HashMap<String,ArrayList<String>> result=new HashMap<>();


            while (condition){

                Integer machineDone=0;
                for (String Sindex:stage.keySet()) {
                    ArrayList<String> command = getCommand(Sindex, stage.get(Sindex),null);
                    if (stage.get(Sindex).equals("stepX")) {
                        machineDone++;
                    }
                    ProcessBuilder builder = new ProcessBuilder(command);
                    try {
                        Process p = builder.start();

                        Thread mythread = new TestSSH(p, queue, Sindex);
                        mythread.start();
                        processs.put(Sindex, p);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                for(String Sindex:stage.keySet()){
                    HashMap<String,ArrayList<String>> r=queue.poll(3,TimeUnit.SECONDS);
                    result.put(r.keySet().toArray()[0].toString(),r.get(r.keySet().toArray()[0]));
                }


                for (String line:result.keySet()){
                    System.out.println(stage.get(line));
                    stage.put(line,getStep(result,line,stage.get(line)));

                }
                System.out.println(stage);




                // Condition d'arret
                if (maxIteration>=10){
                    System.out.println("Trop de tentative");
                    condition= Boolean.FALSE;
                }
                maxIteration++;

                if(machineDone>=stage.keySet().size()){
                    System.out.println("Tous les machines ont finis leur stages");
                    condition=Boolean.FALSE;
                }
            }
            System.out.println("Fin deploiement");
            System.out.println("----------------------------");

//          Mapping
            Integer nbMachine =stage.size();
            File folder = new File("Ressource/split");
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
//                    System.out.println( listOfFiles[i].getName()+" "+ stage.keySet().toArray()[i%nbMachine]);
                    String Sindex = (String) stage.keySet().toArray()[i%nbMachine];
                    ArrayList<String> argss=new ArrayList<>();
                    argss.add("0");
                    argss.add(listOfFiles[i].getName());

                    ArrayList<String> command = getCommand(Sindex, "step5",argss);


                    ProcessBuilder builder = new ProcessBuilder(command);
                    try {
                        Process p = builder.start();

                        Thread mythread = new TestSSH(p, queue, Sindex);
                        mythread.start();
                        processs.put(Sindex, p);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
            for(int i = 0; i < listOfFiles.length; i++){
                HashMap<String,ArrayList<String>> r=queue.poll(6,TimeUnit.SECONDS);
                System.out.println(r);
                String cles =r.get(r.keySet().toArray()[0]).toArray()[1].toString().replace("[","").replace(" ","").replace("]","");



//                ArrayList<String> cles =r.get(r.keySet().toArray()[0]).toArray()[1].toString().replaceAll("[","").replaceAll(" ","").replaceAll("]","");
                for (String cle:cles.split(","))
                {
                    ArrayList<String> values = cleMachine.get(cle);
                    if(values==null){
                        values = new ArrayList<>();
                        values.add(r.get(r.keySet().toArray()[0]).toArray()[0]+"_"+(String)stage.keySet().toArray()[i%nbMachine]);
                        cleMachine.put(cle ,values);

                    }
                    else{
                        values.add(r.get(r.keySet().toArray()[0]).toArray()[0]+"_"+(String)stage.keySet().toArray()[i%nbMachine]);
                        cleMachine.put(cle , values);
                    }


                }


            }

            for (String line:cleMachine.keySet())
            {
                System.out.println(line);
                System.out.println(cleMachine.get(line));

            }
            System.out.println("Fin Mapping");
            System.out.println("----------------------------");
            //Phase copie shuffling

            Integer j=0;
            for (String line:cleMachine.keySet())
            {
                System.out.println(line+" "+stage.keySet().toArray()[j%nbMachine]);
                System.out.println(cleMachine.get(line));
                shuffle(cleMachine.get(line),(String) stage.keySet().toArray()[j%nbMachine],line,j);
                j++;

            }
            System.out.println("Fin Shuffle");
            System.out.println("----------------------------");
            j=0;

            queue = new LinkedBlockingDeque<>();
            processs=new HashMap<>();
            result=new HashMap<>();
            for (String line:cleMachine.keySet())
            {



                ArrayList<String> argss=new ArrayList<>();
                argss.add("2");
                argss.add(line);
                argss.add("/tmp/ctherond/mapResult/SM"+j+".txt");
                argss.add("RM"+j+".txt");

                ArrayList<String> command=getCommand((String) stage.keySet().toArray()[j%nbMachine],"step5",argss);

                ProcessBuilder builder = new ProcessBuilder(command);
                try {
                    Process p = builder.start();

                    Thread mythread = new TestSSH(p, queue, (String) stage.keySet().toArray()[j%nbMachine]);
                    mythread.start();
                    processs.put((String) stage.keySet().toArray()[j%nbMachine], p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                j++;

            }
            j=0;
            machineRN=new HashMap<>();
            System.out.println();
            System.out.println();
            System.out.println("--------------------------");
            System.out.println("FIN MAP REDUCE");
            for(String line:cleMachine.keySet()){
                HashMap<String,ArrayList<String>> r=queue.poll(6,TimeUnit.SECONDS);


                ArrayList<String> values = machineRN.get((String)stage.keySet().toArray()[j%nbMachine]);
                System.out.println(r.get(r.keySet().toArray()[0]).toArray()[0].toString());
                if(values==null){
                    values = new ArrayList<>();
                    values.add(r.get(r.keySet().toArray()[0]).toArray()[1].toString());
                    machineRN.put((String)stage.keySet().toArray()[j%nbMachine] ,values);

                }
                else{
                    values.add(r.get(r.keySet().toArray()[0]).toArray()[1].toString());
                    machineRN.put((String)stage.keySet().toArray()[j%nbMachine] , values);
                }
                j++;
            }
            System.out.println(machineRN);


        }catch (InterruptedException e) {
            e.printStackTrace();
        }






    }
    public static void shuffle(ArrayList<String> sources,String destination,String word,Integer J) throws InterruptedException {

        BlockingDeque<HashMap<String,ArrayList<String>>> queue = new LinkedBlockingDeque<>();
        HashMap<String,Process> processs=new HashMap<>();
        HashMap<String,ArrayList<String>> result=new HashMap<>();

        System.out.println(word);
        for(String source:sources){
            String[] filsou=source.split("_");

            if(!destination.equals(filsou[1])) {
                ArrayList<String> command = new ArrayList<>();
                command.add("scp");
                command.add(filsou[1] + ":/tmp/ctherond/mapResult/" + filsou[0]);
                command.add(destination + ":/tmp/ctherond/mapResult");
                System.out.println(command);
                ProcessBuilder builder = new ProcessBuilder(command);
                try {
                    Process p = builder.start();

                    Thread mythread = new TestSSH(p, queue, filsou[0]);
                    mythread.start();
                    processs.put(filsou[0], p);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        }
        for(String source:sources){

            String[] filsou=source.split("_");
            if(!destination.equals(filsou[1])) {
                HashMap<String,ArrayList<String>> r=queue.poll(6,TimeUnit.SECONDS);
            }


        }

        ArrayList<String> argss=new ArrayList<>();
        argss.add("1");
        argss.add(word);
        argss.add("/tmp/ctherond/mapResult/SM"+J+".txt");
        for(String source:sources) {
            String[] filsou = source.split("_");
            argss.add("/tmp/ctherond/mapResult/"+filsou[0]);
        }
        ArrayList<String> command=getCommand(destination,"step5",argss);

        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            Process p = builder.start();

            Thread mythread = new TestSSH(p, queue, destination);
            mythread.start();
            processs.put(destination, p);
            HashMap<String,ArrayList<String>> r=queue.poll(3,TimeUnit.SECONDS);
            System.out.println(r);
        } catch (IOException e) {
            e.printStackTrace();
        }






    }


    public static ArrayList<String> getCommand(String machine, String step,ArrayList<String> arguments){
        ArrayList<String> command= new ArrayList<String>();
        command.add("ssh");
        command.add(machine);
        // Step1 create directory
        if (step.equals("step1")){
            System.out.println("Creation du repertoire sur "+ machine);
            command.add("mkdir");
            command.add("/tmp/ctherond");
            command.add("/tmp/ctherond/split");
            command.add("/tmp/ctherond/JAR");
            command.add("/tmp/ctherond/mapResult");
            command.add("/tmp/ctherond/reduce");
        }
        // Step2 give right
        if (step.equals("step2")){
            System.out.println("Mise a jour des droits sur"+ machine);
            command.add("chmod");
            command.add("777");
            command.add("/tmp/ctherond");
            command.add("-R");
        }

        if(step.equals("step3")){
            command= new ArrayList<String>();
            final String dir = System.getProperty("user.dir");
            System.out.println("Copies des splits sur"+ machine);
            command.add("scp");
            String textCommand =dir+"/Ressource/split/";
            File folder = new File("Ressource/split");
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    System.out.println("File " + listOfFiles[i].getName());
                    command.add(textCommand+listOfFiles[i].getName());
                }
            }
            command.add(machine+":/tmp/ctherond/split");

        }

        if(step.equals("step4")){
            command= new ArrayList<String>();
            final String dir = System.getProperty("user.dir");
            System.out.println("Copies des jar sur"+ machine);
            command.add("scp");
            String textCommand =dir+"/Ressource/JAR/";
            File folder = new File("Ressource/JAR");
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    System.out.println("File " + listOfFiles[i].getName());
                    command.add(textCommand+listOfFiles[i].getName());
                }
            }
            command.add(machine+":/tmp/ctherond/JAR");

        }

        if (step.equals("step5")){

            System.out.println("Run map sur"+ machine);
            command.add("java");
            command.add("-jar");
            command.add("/tmp/ctherond/JAR/map.jar");
            for(String arg : arguments){
                command.add(arg);
            }

        }
        if (step.equals("stepX")){

            command.add("ls");
        }
        System.out.println(command);

        return command;
    }

    public static String getStep(HashMap<String,ArrayList<String>> result,String line,String step){
        String newStep=step;
        if(step.equals("step1")){

            newStep="step2";
            System.out.println(line);
            System.out.println(step);
            System.out.println(result.get(line));

        }
        if(step.equals("step2")){

            newStep="step3";
            System.out.println(line);
            System.out.println(step);
            System.out.println(result.get(line));

        }
        if(step.equals("step3")){

            newStep="step4";
            System.out.println(line);
            System.out.println(step);
            System.out.println(result.get(line));

        }
        if(step.equals("step4")){

            newStep="stepX";
            System.out.println(line);
            System.out.println(step);
            System.out.println(result.get(line));

        }
        if(step.equals("step5")){

            newStep="stepX";
            System.out.println(line);
            System.out.println(step);
            System.out.println(result.get(line));

        }

        return newStep;
    }



}
