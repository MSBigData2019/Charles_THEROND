package com.master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class CopyThread extends Thread {

        // run() method contains the code that is executed by the thread.
        @Override
        public void run() {
            System.out.println("Inside : " + Thread.currentThread().getName());

            try {
                BufferedReader output = getOutput(process);
                BufferedReader error = getError(process);
                String ligne = "";
                ArrayList<String> result = new ArrayList<String>();


                while ((ligne = output.readLine()) != null) {
                    System.out.println(ligne);
                    result.add(ligne);
                }

                while ((ligne = error.readLine()) != null) {
                    System.out.println(ligne);
                    result.add(ligne);
                }

                queue.put(result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private Process process;
        private BlockingDeque<ArrayList<String>> queue;
        public CopyThread(Process p,BlockingDeque<ArrayList<String>> q){
            this.process=p;
            this.queue=q;
        }
    private static BufferedReader getOutput(Process p) {
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    private static BufferedReader getError(Process p) {
        return new BufferedReader(new InputStreamReader(p.getErrorStream()));
    }


}
