package com.master;

import org.omg.SendingContext.RunTime;
import sun.security.x509.AttributeNameEnumeration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static java.lang.Boolean.FALSE;

public class Main {
    public static void main(String[] args) {
        BlockingDeque<ArrayList<String>> queue = new LinkedBlockingDeque<>();
        List<String> command= new ArrayList<String>();
        Integer len = args.length;
        System.out.println(len);
        for(Integer i=0; i < len;i++){
            if(args[i].equals("scp")){
                command= new ArrayList<String>();
                command.add("scp");
                command.add(args[i+1]);
                command.add(args[i+2]);
                ProcessBuilder builder = new ProcessBuilder(command);
                try {
                    Process p = builder.start();
                    Thread copythread = new CopyThread(p,queue);
                    System.out.println("Starting thread...");
                    copythread.start();
                    ArrayList<String> result =queue.poll(2, TimeUnit.SECONDS);
                    System.out.println(result);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            if(args[i].equals("start")){
                command= new ArrayList<String>();
                execJar(args[i+1],args[i+2]);

            }//*/
        }

        command= new ArrayList<String>();
        command.add("ssh");
        command.add("ctherond@c133-05.enst.fr");
        command.add("java");
        command.add("-jar");
        command.add("/tmp/ctherond/INF727.jar");
        String[] params= "/tmp/ctherond/sante_publique.txt 10 0 ".split(" ");
        for (String param : params){
            command.add(param);
        }
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            Process p = builder.start();
            Thread copythread = new CopyThread(p,queue);
            System.out.println("Starting thread...");
            copythread.start();
            ArrayList<String> result =queue.poll(5, TimeUnit.SECONDS);
            System.out.println(result);
            if(result==null){
                p.destroy();
                System.out.println("TIMEOUT");
                System.out.println("Nous n'avons pas execute :");
                System.out.println(command);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /**try {
            copythread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }

    public static void copyFile(String fileName, String destination){
        List<String> command= new ArrayList<String>();
        command.add("scp");
        command.add(fileName);
        command.add(destination);
        System.out.println(command);
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            Process p = builder.start();
            BufferedReader output = getOutput(p);
            BufferedReader error = getError(p);
            String ligne = "";

            while ((ligne = output.readLine()) != null) {
                System.out.println(ligne);
            }

            while ((ligne = error.readLine()) != null) {
                System.out.println(ligne);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void execJar(String nomJar,String argument){
        List<String> command= new ArrayList<String>();
        BlockingDeque<Thread> queue = new LinkedBlockingDeque<>();
        command.add("ssh");
        command.add("ctherond@c133-05.enst.fr");
        command.add("java");
        command.add("-jar");
        command.add("/tmp/ctherond/INF727.jar");

        String[] params= argument.split(" ");
        for (String param : params){
            command.add(param);
        }
        System.out.println(command);

        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            Process p = builder.start();
            queue.poll(2,TimeUnit.SECONDS);
            BufferedReader output = getOutput(p);
            BufferedReader error = getError(p);
            String ligne = "";


            while ((ligne = output.readLine()) != null) {
                System.out.println(ligne);
            }

            while ((ligne = error.readLine()) != null) {
                System.out.println(ligne);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private static BufferedReader getOutput(Process p) {
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    private static BufferedReader getError(Process p) {
        return new BufferedReader(new InputStreamReader(p.getErrorStream()));
    }

//   java -jar master.jar "scp" "sante_publique.txt" "ctherond@c133-05.enst.fr:/tmp/ctherond" "scp" "INF727.jar" "ctherond@c133-05.enst.fr:/tmp/ctherond" "start" "/tmp/ctherond/INF727.jar" "/tmp/ctherond/sante_publique.txt 10 0 "
}
