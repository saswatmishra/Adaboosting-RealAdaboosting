package adaboost;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Saswat
 */
public class File_info {
    int T,n;
    float epsila;
    float[] x;
    int[] y;
    float[] p;
    HashMap<Integer,String> weakClassifier;
 public File_info(String filename)
 {
        try {
            FileInputStream fis = null;
            //fis = new FileInputStream(file);
            fis = new FileInputStream("");
            //exception catch 
            //file =data.txt
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            String[] n1 = dis.readLine().split(" ");
            this.T=Integer.parseInt(n1[0]);
            this.n=Integer.parseInt(n1[1]);
            this.epsila=Float.parseFloat(n1[2]);
            x=new float[n];
            y=new int[n];
            p=new float[n];
            n1=null;
            n1=dis.readLine().split(" ");
            String[] n2=dis.readLine().split(" ");
            String[] n3=dis.readLine().split(" ");
            System.out.println(T+"\t"+n+"\t"+epsila);
            System.out.println(n1.length);
            weakClassifier = new HashMap<Integer, String>();
            for(int i=0;i<n;i++)
            {
              x[i]=Float.parseFloat(n1[i]);
              y[i]=Integer.parseInt(n2[i]);
              p[i]=Float.parseFloat(n3[i]);
              System.out.println(x[i]+"\t"+y[i]+"\t"+p[i]);
            }
            for(int i=0;i<n-1;i++)
            {
              if(y[i]*y[i+1]==-1)
              {
                int l=0,r=0;
                for(int j=0;j<(i+1);j++)
                {
                  if(y[j]==-1)
                  {
                  l=l+1;
                  }
                }

                for(int j=(i+1);j<n;j++)
                {
                  if(y[j]==1)
                  {
                  l=l+1;
                  }
                }
                //if(l<(n-l))
                {
                  weakClassifier.put(weakClassifier.size(),"<"+","+(((float)x[i]+x[i+1])/2));
                }
                //else
                {
                   weakClassifier.put(weakClassifier.size(),">"+","+(((float)x[i]+x[i+1])/2));
                }


              }
            }

            //for(int i=0;i<weakClassifier.size();i++)
            {
                System.out.println(weakClassifier.keySet());
                System.out.println(weakClassifier.values());
            }

        } catch (Exception ex) {
            Logger.getLogger(File_info.class.getName()).log(Level.SEVERE, null, ex);
        } 
 }

 public static void main(String args[]) 
 {
   File_info f=new File_info("data.txt");
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Calendar cal = Calendar.getInstance();
   System.out.println(dateFormat.format(cal.getTime()));
   String start=dateFormat.format(cal.getTime());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(File_info.class.getName()).log(Level.SEVERE, null, ex);
        }
   DateFormat dateF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

   Calendar cal1 = Calendar.getInstance();
   System.out.println(dateFormat.format(cal1.getTime()));
  String end=dateF.format(cal1.getTime());
  System.out.print(start.split(" ")[1]+","+end.split(" ")[1]);

 }
}
