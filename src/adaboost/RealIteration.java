package adaboost;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Saswat
 */
public class RealIteration {
int no=1;
int h_t;        // selected weak classifier number(ID)
float G;
float cplus;
float cminus;//error of h_t
float pr_plus;
float pr_minus;
float pw_plus;
float pw_minus;
float Z_t;   // probabilities normalization factor
float p_t[];
float g_t[];
float f_t[];// new probabilities
HashMap<String,Float> f_t1; // boosted Classifier stored
float E_t=0; // error of Boosted classifier f_t
float piZ_t; // bound on Z_t
int mistakes=0;

void print_into_file(File_info f,String x)
    {
        FileWriter fstream = null;
        try {
            Boolean flag=true;
            if(no==1)
            {
              flag=false;
            }
            fstream = new FileWriter("C:\\Users\\Saswat\\Documents\\MS-2nd sem\\Machine Learning\\inputfiles\\data1.txt"+x,flag);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("Iteration Number "+no);
            out.newLine();
            //out.write("pr+"+pr_plus+"pr-"+pr_minus);
            //out.newLine();
            //out.write("pw+"+pw_plus+"pw-"+pw_minus);
            //out.newLine();
            out.write("1)weak classifier selected: ");
            out.write("h(x)=I(x"+f.weakClassifier.get(h_t).split(",")[0]+f.weakClassifier.get(h_t).split(",")[1]+")");
            out.newLine();
            out.write("2)error of Ht:\t"+G+"\n");
            out.newLine();
            out.write("3)weights of c+ & c-:");
            out.newLine();
            out.write("c+:\t"+cplus);
            out.newLine();
            out.write("c-:\t"+cminus);
            out.newLine();
            out.write("4)probabilities normalization vector(Zt):\t"+Z_t+"\n");
            out.newLine();
            out.write("5,6)Updated Probablities AND VALUES OF ft(x) ") ;
            out.newLine();
            out.write("Xi\tPi\tft(Xi)");
            for(int i=0;i<p_t.length;i++)
            {
              out.newLine();
              out.write("\n"+f.x[i]+"\t"+p_t[i]+"\t"+f_t[i]);
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
public RealIteration(File_info f)
{
    float min=100;
    int index=0;
    p_t=new float[f.n];
    f_t=new float[f.n];
    g_t=new float[f.n];
    

    for(int i=0;i<f.weakClassifier.size();i++)
        {
        float t_pr_plus=0;
    float t_pr_minus=0;
    float t_pw_plus=0;
    float t_pw_minus = 0;
          //f_t.put(f.weakClassifier.get(i).toString(),(float)0.0);
          float error=0;  
          String temp=f.weakClassifier.get(i).toString();
          int flow;
          float range=Float.parseFloat(temp.split(",")[1]);
          System.out.println("range"+range);
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
                if(flow==1)
                {
                 t_pw_minus+=f.p[j];
                 System.out.println(f.x[j]+","+f.y[j]+","+range+"pw-");
                }
                else
                {
                  t_pw_plus+=f.p[j];
                  System.out.println(f.x[j]+","+f.y[j]+","+range+"pw+");
                }
                 
              }

              else
              {
                 if(flow==1)
                {
                  t_pr_plus+=f.p[j];
                  System.out.println(f.x[j]+","+f.y[j]+","+range+"pr+");
                }
                else
                {
                   t_pr_minus+=f.p[j];
                   System.out.println(f.x[j]+","+f.y[j]+","+range+"pr-");
                }

              }

           }
           if(f.x[j]>range)
           {
              if(f.y[j]==flow)
              {
                if(flow==1)
                {
                 t_pw_plus+=f.p[j];
                 System.out.println(f.x[j]+","+f.y[j]+","+range+"pw+");
                }
                else
                {
                  t_pw_minus+=f.p[j];
                  System.out.println(f.x[j]+","+f.y[j]+","+range+"pw-");
                }

              }
              else
              {
                 if(flow==1)
                {
                  t_pr_minus+=f.p[j];
                  System.out.println(f.x[j]+","+f.y[j]+","+range+"pr-");
                }
                else
                {
                   t_pr_plus+=f.p[j];
                   System.out.println(f.x[j]+","+f.y[j]+","+range+"pr+");
                }

              }

           }
          }
          System.out.println(t_pr_plus+","+t_pr_minus+","+t_pw_plus+","+t_pw_minus);
          error=(float) (Math.sqrt(t_pr_plus * t_pw_minus) + Math.sqrt(t_pr_minus * t_pw_plus));
          if(error<min)
          {
            System.out.println("flow"+flow);
            min=error;
            index=i;
            pr_minus=t_pr_minus;
            pr_plus=t_pr_plus;
            pw_minus=t_pw_minus;
            pw_plus=t_pw_plus;


          }
        }
    G=min;
    h_t=index;
    cplus=(float) (Math.log(pr_plus + f.epsila) - Math.log(pw_minus + f.epsila));
    cplus=cplus/2;
    cminus=(float) (Math.log(pw_plus + f.epsila) - Math.log(pr_minus + f.epsila));
    cminus=cminus/2;
     String temp=f.weakClassifier.get(h_t).toString();
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

     Z_t=  (float) ((Math.sqrt(4*pr_plus * pw_minus) + Math.sqrt(4*pr_minus * pw_plus)));
    for(int i=0;i<f.n;i++)
    {
      if(f.x[i]<=range)
      {
       if(flow==1)
       {
         g_t[i]=cplus;
       }
       else
       {
          g_t[i]=cminus;
       }
      }
      if(f.x[i]>range)
      {
       if(flow==1)
       {
         g_t[i]=cminus;
       }
       else
       {
          g_t[i]=cplus;
       }
      }
      p_t[i]=(float) ((f.p[i] * Math.exp(-1 * f.y[i] * g_t[i]))/Z_t);
      f_t[i]=g_t[i];
    }
    for(int i=0;i<f.n;i++)
    {
      if(f_t[i]>0)
      {
        if(f.y[i]!=1)
        {
          mistakes+=1;
        }
      }
      if(f_t[i]<0)
      {
        if(f.y[i]!=-1)
        {
          mistakes+=1;
        }
      }
    }
    E_t=(float)(mistakes)/f.n;
    piZ_t=Z_t;
    //printing values for testing

   }

public RealIteration(File_info f,RealIteration IR,int k)
{
    no=k;
    float min=100;
    int index=0;
    p_t=new float[f.n];
    f_t=new float[f.n];
    g_t=new float[f.n];


    for(int i=0;i<f.weakClassifier.size();i++)
        {
        float t_pr_plus=0;
        float t_pr_minus=0;
        float t_pw_plus=0;
        float t_pw_minus = 0;
          //f_t.put(f.weakClassifier.get(i).toString(),(float)0.0);
        float error=0;
        String temp=f.weakClassifier.get(i).toString();
        int flow;
        float range=Float.parseFloat(temp.split(",")[1]);
        System.out.println("range"+range);
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
                if(flow==1)
                {
                 t_pw_minus+=IR.p_t[j];
                 System.out.println(f.x[j]+","+f.y[j]+","+range+"pw-");
                }
                else
                {
                  t_pw_plus+=IR.p_t[j];
                  System.out.println(f.x[j]+","+f.y[j]+","+range+"pw+");
                }

              }

              else
              {
                 if(flow==1)
                {
                  t_pr_plus+=IR.p_t[j];
                  System.out.println(f.x[j]+","+f.y[j]+","+range+"pr+");
                }
                else
                {
                   t_pr_minus+=IR.p_t[j];
                   System.out.println(f.x[j]+","+f.y[j]+","+range+"pr-");
                }

              }

           }
           if(f.x[j]>range)
           {
              if(f.y[j]==flow)
              {
                if(flow==1)
                {
                 t_pw_plus+=IR.p_t[j];
                 System.out.println(f.x[j]+","+f.y[j]+","+range+"pw+");
                }
                else
                {
                  t_pw_minus+=IR.p_t[j];
                  System.out.println(f.x[j]+","+f.y[j]+","+range+"pw-");
                }

              }
              else
              {
                 if(flow==1)
                {
                  t_pr_minus+=IR.p_t[j];
                  System.out.println(f.x[j]+","+f.y[j]+","+range+"pr-");
                }
                else
                {
                   t_pr_plus+=IR.p_t[j];
                   System.out.println(f.x[j]+","+f.y[j]+","+range+"pr+");
                }

              }

           }
          }
          System.out.println(t_pr_plus+","+t_pr_minus+","+t_pw_plus+","+t_pw_minus);
          error=(float) (Math.sqrt(t_pr_plus * t_pw_minus) + Math.sqrt(t_pr_minus * t_pw_plus));
          if(error<min)
          {
            System.out.println("flow"+flow);
            min=error;
            index=i;
            pr_minus=t_pr_minus;
            pr_plus=t_pr_plus;
            pw_minus=t_pw_minus;
            pw_plus=t_pw_plus;


          }
        }
    G=min;
    h_t=index;
    cplus=(float) (Math.log(pr_plus + f.epsila) - Math.log(pw_minus + f.epsila));
    cplus=cplus/2;
    cminus=(float) (Math.log(pw_plus + f.epsila) - Math.log(pr_minus + f.epsila));
    cminus=cminus/2;
    String temp=f.weakClassifier.get(h_t).toString();
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
     Z_t=(float) ((Math.sqrt(4*pr_plus * pw_minus) + Math.sqrt(4*pr_minus * pw_plus)));
    for(int i=0;i<f.n;i++)
    {
      if(f.x[i]<=range)
      {
       if(flow==1)
       {
         g_t[i]=cplus;
       }
       else
       {
          g_t[i]=cminus;
       }
      }
      if(f.x[i]>range)
      {
       if(flow==1)
       {
         g_t[i]=cminus;
       }
       else
       {
          g_t[i]=cplus;
       }
      }
      p_t[i]=(float) ((IR.p_t[i] * Math.exp(-1 * f.y[i] * g_t[i]))/Z_t);
      f_t[i]=g_t[i]+IR.f_t[i];
    }
    for(int i=0;i<f.n;i++)
    {
      if(f_t[i]>0)
      {
        if(f.y[i]!=1)
        {
          mistakes+=1;
        }
      }
      if(f_t[i]<0)
      {
        if(f.y[i]!=-1)
        {
          mistakes+=1;
        }
      }
    }
    E_t=(float)(mistakes)/f.n;
    piZ_t=Z_t*IR.piZ_t;

}


public static void main(String args[])
{
  String x="data.txt";
  File_info f=new File_info(x);
  RealIteration RI[]=new RealIteration[f.T];
  RI[0]=new RealIteration(f);
  RI[0].print_into_file(f,x);
  for(int i=1;i<f.T;i++)
  {
      RI[i]=new RealIteration(f,RI[i-1], i+1);
      RI[i].print_into_file(f,x);
  }
}

}
