/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsh;

public class Table
{
   public Nodes node;
   public String value;
   public int globalDepth;
    boolean isX=false;
    public Table(){}
    public Table(String v,int depth)
    {
        value=v;
        globalDepth=depth;
    }
    public Table(String v)
    {
        value=v;
    }
}
