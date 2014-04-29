package adaboost;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Saswat
 */
public class Adaboosting {
public static void main(String args[])
    {
     String x="data2.txt";
      File_info f=new File_info(x);
      Iteration I[]=new Iteration[f.T];
      I[0]=new Iteration(f);
      I[0].print_into_file(f,x);
      for(int i=1;i<f.T;i++)
      {
        I[i]=new Iteration(f,I[i-1],i+1);
        I[i].print_into_file(f,x);
      }
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
