package adaboost;


import java.util.HashMap;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author saswat
 */
public class Iteration {


int no=1;
int h_t;        // selected weak classifier number(ID)
float e_t;     //error of h_t
float a_t;    // weight of h_t
float Z_t;   // probabilities normalization factor
float p_t[]; // new probabilities
HashMap<String,Float> f_t; // boosted Classifier stored
float E_t=0; // error of Boosted classifier f_t
float piZ_t; // bound on Z_t
int mistakes=0;
    Iteration(File_info f,Iteration x,int k)
    {
        System.out.println("Iteration "+k);
       no=k;
       float min=1;
       int index=0;
       p_t=new float[f.n];
       f_t=new HashMap<String, Float>();
       for(int i=0;i<f.weakClassifier.size();i++)
        {
          f_t.put(f.weakClassifier.get(i).toString(),(float)x.f_t.get(f.weakClassifier.get(i).toString()));
          float error=0;
          String temp=f.weakClassifier.get(i).toString();
          int flow;
          float range=Float.parseFloat(temp.split(",")[1]);
          if(temp.split(",")[0].equals("<"))
          {
            flow=1;
          }
          else
          {
            flow=-1;
          }
          for(int j=0;j<f.n;j++)
          {
           if(f.x[j]<=range)
           {
              if(f.y[j]!=flow)
              {
                 error=error+x.p_t[j];
              }
           }
           if(f.x[j]>range)
           {
              if(f.y[j]!=(flow*-1))
              {
                 error=error+x.p_t[j];
              }
           }
          }
          if(error<min)
          {
            min=error;
            index=i;
          }
        }
      h_t=index;
      e_t=min;
      a_t=(float) (Math.log(1 - e_t) - Math.log(e_t));
      a_t=a_t/2;
      Z_t=(float) Math.sqrt(e_t*(1-e_t));
      Z_t=2*Z_t;
      float te=f_t.get(f.weakClassifier.get(index).toString());
      f_t.remove(f.weakClassifier.get(index).toString());
      f_t.put(f.weakClassifier.get(index).toString(), (a_t+te));
      piZ_t=Z_t*x.piZ_t;
      System.out.println("f(x)="+f_t.entrySet());
      System.out.println(h_t+" "+e_t+" "+a_t+" "+Z_t+" ");
      float q1=(float) Math.exp(a_t);
      float q2=(float) Math.exp(-1*a_t);
      for(int i=0;i<f.n;i++)
      {
         float q;
         if(newReturnFunc(f, i)==1)
                  {
           // System.out.println("return function value for"+returnFunc(f, i));
            q=q2;
            p_t[i]=x.p_t[i]*q/Z_t;
         }
         else
         {
            q=q1;
            p_t[i]=x.p_t[i]*q/Z_t;
            E_t=E_t+x.p_t[i];
            //mistakes+=1;
         }
         if(returnFunc(f, i)==-1)
         {
           mistakes+=1;
         }
         E_t=(float)(mistakes)/f.n;
         
         System.out.print(p_t[i]+" ");
      }
      System.out.println("\n"+E_t+"\t"+mistakes);
    }
int newReturnFunc(File_info f,int j)
{
    int x=0;
    String temp=f.weakClassifier.get(h_t).toString();
    int flow;
    if(temp.split(",")[0].equals("<"))
     {
      flow=1;
      }
      else
     {
      flow=-1;
     }
    if(f.x[j]<=Float.parseFloat(temp.split(",")[1]))
          {
              if(f.y[j]!=flow)
              {
                x=-1;
              }
              else
              {
                 x=1;
              }
          }
          if(f.x[j]>Float.parseFloat(temp.split(",")[1]))
          {
              if(f.y[j]!=(flow*-1))
              {
                x=-1;
              }
              else
              {
                 x=1;
              }
          }
    return x;
}
/*
int returnFunc(Iteration I,File_info f,int j)
    {
      float x=0;
      for(int i=0;i<f_t.size();i++)
      {
          String temp=f.weakClassifier.get(i).toString();
          int flow;
          if(temp.split(",")[0].equals("<"))
          {
            flow=1;
          }
          else
          {
            flow=-1;
          }
         // System.out.println("flow"+flow);
          if(f.x[j]<=Float.parseFloat(temp.split(",")[1]))
          {
              if(f.y[j]!=flow)
              {
                x=x-f_t.get(temp);
              }
              else
              {
                 x=x+f_t.get(temp);
              }
          }
          if(f.x[j]>Float.parseFloat(temp.split(",")[1]))
          {
              if(f.y[j]!=(flow*-1))
              {
                x=x-f_t.get(temp);
              }
              else
              {
                 x=x+f_t.get(temp);
              }
          }
          //System.out.println();
      }
      if(x>0)
      {
       return 1;
      }
      return -1;
    }
*/
    Iteration(File_info f)
    {

      //calculate hypothesis with min error
        float min=1;
        int index=0;
        p_t=new float[f.n];
        f_t=new HashMap<String, Float>();
        for(int i=0;i<f.weakClassifier.size();i++)
        {
          f_t.put(f.weakClassifier.get(i).toString(),(float)0.0);  
          float error=0;  
          String temp=f.weakClassifier.get(i).toString();
          int flow;
          float range=Float.parseFloat(temp.split(",")[1]);
          if(temp.split(",")[0].equals("<"))
          {
            flow=1;  
          }
          else
          {
            flow=-1;  
          }
          for(int j=0;j<f.n;j++)
          {
           if(f.x[j]<=range)
           {
              if(f.y[j]!=flow)
              {
                 error=error+f.p[j]; 
              }
           }
           if(f.x[j]>range)
           {
              if(f.y[j]!=(flow*-1))
              {
                 error=error+f.p[j]; 
              }
           }
          }
          if(error<min)
          {
            min=error;
            index=i;
          }
        }

      h_t=index;
      e_t=min;
      a_t=   (float) (Math.log(1 - e_t) - Math.log(e_t));
      a_t=a_t/2;
      Z_t=(float) Math.sqrt(e_t*(1-e_t));
      Z_t=2*Z_t;
      f_t.remove(f.weakClassifier.get(index).toString());
      f_t.put(f.weakClassifier.get(index).toString(), a_t);
      piZ_t=Z_t;
      System.out.println("f(x)="+f_t.entrySet());

      System.out.println(h_t+" "+e_t+" "+a_t+" "+Z_t+" ");
      float q1=(float) Math.exp(a_t);
      float q2=(float) Math.exp(-1*a_t);
      for(int i=0;i<f.n;i++)
      {
         float q;
         if(returnFunc(f,i)==1)
         {
           // System.out.println("return function value for"+returnFunc(f, i));
            q=q2;
            p_t[i]=f.p[i]*q/Z_t;
         }
         else
         {
            q=q1;
            p_t[i]=f.p[i]*q/Z_t;
            E_t=E_t+p_t[i];
            mistakes+=1;
         }
         E_t=(float)(mistakes)/f.n;
         
         System.out.print(p_t[i]+" ");
      }
      System.out.println("\n"+E_t+"\t"+mistakes);

    }
    
    int returnFunc(File_info f,int j)
    {
      float x=0;  
      for(int i=0;i<f_t.size();i++)
      {
          String temp=f.weakClassifier.get(i).toString();
          int flow;
          if(temp.split(",")[0].equals("<"))
          {
            flow=1;  
          }
          else
          {
            flow=-1;  
          }
         // System.out.println("flow"+flow);
          if(f.x[j]<=Float.parseFloat(temp.split(",")[1]))
          {
              if(f.y[j]!=flow)
              {
                x=x-f_t.get(temp);
              }
              else
              {
                 x=x+f_t.get(temp);
              }
          }
          if(f.x[j]>Float.parseFloat(temp.split(",")[1]))
          {
              if(f.y[j]!=(flow*-1))
              {
                x=x-f_t.get(temp);
              }
              else
              {
                 x=x+f_t.get(temp);
              }
          }
          //System.out.println();
      }
      if(x>0)
      {
       return 1;
      }
      return -1;  
    }

    void print_into_file(File_info f,String x)
    {
        FileWriter fstream = null;
        try {
            Boolean flag=true;
            if(no==1)
            {
              flag=false;
            }
            fstream = new FileWriter("",flag);//file path
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("Iteration Number "+no);
            out.newLine();
            out.write("1)weak classifier selected: ");
            out.write("h(x)=I(x"+f.weakClassifier.get(h_t).split(",")[0]+f.weakClassifier.get(h_t).split(",")[1]+")");
            out.newLine();
            out.write("2)error of Ht:\t"+e_t+"\n");
            out.newLine();
            out.write("3)weight of Ht:\t"+a_t+"\n");
            out.newLine();
            out.write("4)probabilities normalization vector(Zt):\t"+Z_t+"\n");
            out.newLine();
            out.write("5)Updated Probablities ") ;
            out.newLine();
            out.write("Xi\tPi");
            for(int i=0;i<p_t.length;i++)
            {
              out.newLine();
              out.write("\n"+f.x[i]+"\t"+p_t[i]);
            }
            out.newLine();
            out.write("6)Boosted Classifier:"+"\n");
            //out.newLine();
            out.write("f(x)=");
            for(int i=0;i<f_t.size();i++)
            {
             if(f_t.get(f.weakClassifier.get(i))!=0)
             {
             if(i>0)
             {
                 out.write("+");
             }
             String temp="I(x"+f.weakClassifier.get(i).split(",")[0]+f.weakClassifier.get(i).split(",")[1]+")";
             out.write(f_t.get(f.weakClassifier.get(i))+temp);
             
            //out.write(f_t.entrySet().toString()+"\n");
             }
            }
            out.newLine();
            out.write("7)error of Boosted Classifier:\t"+E_t+"\n");
            out.newLine();
            out.write("8)Bound on E_t:\t"+piZ_t+"\n");
            out.newLine();
            out.write("Mistakes:\t"+mistakes+"\n");
            out.newLine();
            out.write("********************************************"+"\n");
            out.newLine();
            out.close();
            fstream.close();
        } catch (IOException ex) {
            Logger.getLogger(Iteration.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public static void main(String args[])
    {
      File_info f=new File_info("data1.txt");
      Iteration I[]=new Iteration[f.T];
      I[0]=new Iteration(f);
      I[0].print_into_file(f,"data1.txt");
      for(int i=1;i<f.T;i++)
      {
        I[i]=new Iteration(f,I[i-1],i+1);
        I[i].print_into_file(f,"data1.txt");
      }
    }
}
