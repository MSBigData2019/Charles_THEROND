package com.master;

import sun.security.x509.AttributeNameEnumeration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Integer len = args.length;
        System.out.println(len);
        for(Integer i=0; i < len;i++){
            if(args[i].equals("scp")){
                copyFile(args[i+1],args[i+2]);
            }
            if(args[i].equals("start")){
                execJar(args[i+1],args[i+2]);

            }
        }

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
        command.add("ssh");
        command.add("ctherond@c133-05.enst.fr");
        command.add("java");
        command.add("-jar");
        command.add(nomJar);
        String[] params= argument.split(" ");
        for (String param : params){
            command.add(param);
        }
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
    private static BufferedReader getOutput(Process p) {
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    private static BufferedReader getError(Process p) {
        return new BufferedReader(new InputStreamReader(p.getErrorStream()));
    }

//   java -jar master.jar "scp" "sante_publique.txt" "ctherond@c133-05.enst.fr:/tmp/ctherond" "scp" "INF727.jar" "ctherond@c133-05.enst.fr:/tmp/ctherond" "start" "/tmp/ctherond/INF727.jar" "/tmp/ctherond/sante_publique.txt 10 0 "
}
