package com.deploiement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingDeque;

public class TestSSH extends Thread{
    private  String machine;

    private Process process;
    private BlockingDeque<HashMap<String,ArrayList<String>>> queue;

    // run() method contains the code that is executed by the thread.
    @Override
    public void run() {
        try {
//            this.sleep(1000);
            BufferedReader output = getOutput(process);
            BufferedReader error = getError(process);
            String ligne = "";
            ArrayList<String> result = new ArrayList<String>();
            HashMap<String,ArrayList<String>>R=new HashMap<>();

            while ((ligne = output.readLine()) != null) {
                result.add(ligne);
            }

            while ((ligne = error.readLine()) != null) {
                result.add(ligne);
            }//*/
            R.put(machine,result);
            queue.put(R);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public TestSSH(Process p, BlockingDeque<HashMap<String,ArrayList<String>>> q,String m){
        this.process=p;
        this.queue=q;
        this.machine=m;
    }
    private static BufferedReader getOutput(Process p) {
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    private static BufferedReader getError(Process p) {
        return new BufferedReader(new InputStreamReader(p.getErrorStream()));
    }
}
