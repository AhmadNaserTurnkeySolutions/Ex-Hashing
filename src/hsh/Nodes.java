/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsh;


public class Nodes 
{
   public int []values=new int[4];
   public int localDepth;
    public Nodes()
    {        
        for(int i=0;i<4;i++)
            values[i]=-1;
    }
    public int getEmpty()
    {                
        for(int i=0;i<4;i++)
            if(values[i]==-1)
                return i;
        return -1;//not empty
    }
    public boolean isEmpty()
    {
        boolean empty=true;
        for(int i=0;i<4;i++)
            if(values[i]!=-1)
                empty=false;
        return empty;
    }
}
