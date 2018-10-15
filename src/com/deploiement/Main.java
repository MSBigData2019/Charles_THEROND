package com.deploiement;

import com.master.CopyThread;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static jdk.nashorn.internal.objects.ArrayBufferView.length;


public class Main {

    public static HashMap<String,String> stage = new HashMap<>();

    public static void main(String[] args){
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
                command.add("java");
                command.add("-version");
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
                HashMap<String,ArrayList<String>> r=queue.poll(1,TimeUnit.SECONDS);
                result.put(r.keySet().toArray()[0].toString(),r.get(r.keySet().toArray()[0]));

            }

            for (String line:result.keySet()){
                //System.out.println(line);
                //System.out.println(result.get(line));
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

        Boolean condition= Boolean.TRUE;
        Integer maxIteration= 0;
        while (condition){


            // Condition d'arret
            if (maxIteration>=1000){
                System.out.println("Trop de tentative");
                condition= Boolean.FALSE;
            }
            maxIteration++;
            Integer mahcineDone=0;
            for (String Sindex:stage.keySet()){
                if(stage.get(Sindex).equals("step1")){
                    mahcineDone++;
                }
            }
            if(mahcineDone>=stage.keySet().size()){
                System.out.println("Tous les machines ont finis leur stages");
                condition=Boolean.FALSE;
            }
        }
        System.out.println(stage);
        System.out.println("Fin du proccess");

    }



}
