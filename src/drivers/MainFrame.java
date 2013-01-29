/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package drivers;

import bzu.hashing.LinePaint;
import bzu.hashing.NodePaint;
import bzu.hashing.Nodes;
import bzu.hashing.OutputFrame;
import bzu.hashing.Table;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;


public class MainFrame extends javax.swing.JFrame implements Runnable,ActionListener {

    /** Creates new form MainFrame */
    ArrayList<Table>list=new ArrayList<Table>();
    ArrayList<LinePaint>graphList;
    ArrayList<NodePaint>nodeGraph;
    int lastX,LastY,New,LastNode;
      JPopupMenu Pmenu;
  JMenuItem menuItem,t1,t2,t3,t4,t5,t6,t7;
    public MainFrame()
    {
        initComponents();
       // this.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\hash-icon.png"));
        //this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH); work if doesn't make
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(Toolkit.getDefaultToolkit().getClass().getResource("/bzu/hashing/hash-icon.png")));

//        this.setSize(width,height);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        firstAppend();
        Thread t=new Thread(this);
        t.start();
        
        
        
        
        
        
        
        
        
        
        
         Pmenu = new JPopupMenu();
  t1= new JMenuItem("Insert");
    Pmenu.add(t1);
      t2= new JMenuItem("Delete");
    Pmenu.add(t2);
      t3= new JMenuItem("Read From File");
    Pmenu.add(t3);
      t4= new JMenuItem("Show Output Text");
    Pmenu.add(t4);
          t5= new JMenuItem("New");
    Pmenu.add(t5);
      t6= new JMenuItem("Exit");
    Pmenu.add(t6);

    t1.addActionListener(this);
   t2.addActionListener(this);
      t3.addActionListener(this);
         t4.addActionListener(this);
            t5.addActionListener(this);
               t6.addActionListener(this);
            
this.HashPanel.addMouseListener(new MouseAdapter(){
      public void mouseReleased(MouseEvent Me){
        if(Me.isPopupTrigger()){
          Pmenu.show(Me.getComponent(), Me.getX(), Me.getY());
        }
      }
    });
    
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        /*******************************************************************************/
        
              fullScreen();
    }
    private void readFromFile(String fName)
    {
        File file=new File(fName);
        Scanner scan = null;
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        int insert;
        while(scan.hasNext())
        {
            insert=Integer.parseInt(scan.next());
            Insert(insert);
        }
        //Print();
        Thread t=new Thread(this);
        t.start();
          

    }
    private void Insert(int x)
    {
        if(!find(x))
        {
            this.New=x;
            int hash=x%(list.size()-1);
            hash+=1;//because we have a header
            //boolean isEmpty=false;
            if(list.get(hash).node==null)
            {
                Nodes node=new Nodes();
                node.localDepth=list.get(0).globalDepth;
                node.values[0]=x;
                list.get(hash).node=node;
            }
            else
            {
                int Empty=list.get(hash).node.getEmpty();
                if(Empty!=-1)
                {
                    list.get(hash).node.values[Empty]=x;
                }
                if(Empty==-1)
                {
                    if(list.get(hash).node.localDepth==list.get(0).globalDepth)
                    {
                        //rehashing
                        Expand();
                        Nodes node=list.get(hash).node;
                        checkEqual(node);//find another pointer
                        list.get(hash).node=null;
                        int index;
                        for(int i=0;i<node.values.length;i++)
                        {
                            index=node.values[i]%(list.size()-1);
                            index+=1;
                            if(list.get(index).node==null)
                            {
                                Nodes n=new Nodes();
                                n.values[0]=node.values[i];
                                n.localDepth=list.get(0).globalDepth;
                                list.get(index).node=n;
                            }
                            else
                            {
                                int k=list.get(index).node.getEmpty();
                                if(k!=-1)
                                {
                                    list.get(index).node.values[k]=node.values[i];
                                }
                            }
                        }
                        //if(list.get(hash).node!=null)
                        //list.get(hash).node.values[list.get(hash).node.isEmpty()]=x;
                        //else
                        //{
                        //Nodes n=new Nodes();
                        //n.localDepth=list.get(0).globalDepth;
                        //n.values[0]=x;
                        //list.get(hash).node=n;
                        //}
                        Insert(x);//New 21/10/2011
                        checkPointers();
                    }
                    else//split and make globaldepth=localdepth
                    {
                        //there's case need's rehashing
                        int localdepth=list.get(hash).node.localDepth;
                        localdepth+=1;//changed
                        int denominator =(int) Math.pow(2, localdepth);
                        Nodes node=list.get(hash).node;
                        checkEqual(node);//find another pointer for delete the pointer
                        list.get(hash).node=null;
                        int index;
                        for(int value : node.values)//Reorder old valuese in node
                        {
                           index=value%denominator;
                           index+=1;
                           if(list.get(index).node==null)
                           {
                               Nodes n=new Nodes();
                               n.values[0]=value;
                               list.get(index).node=n;
                               list.get(index).node.localDepth=localdepth;//change for all
                           }
                           else
                           {
                               int empty=list.get(index).node.getEmpty();
                               if(empty!=-1)
                               {
                                   list.get(index).node.values[empty]=value;

                               }
                               else
                               {
                                   Insert(value);
                               }
                           }
                        }
                        int empty=list.get(hash).node.getEmpty();
                        if(empty!=-1)
                        {
                            list.get(hash).node.values[empty]=x;
                        }
                        else
                            Insert(x);
                    }
                }
            }
       }
        else
        {
            JOptionPane.showMessageDialog(null, "This number is already inserted , Please insert another number","Error Duplicate", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void Delete(int number)
    {
        int hash=number%(list.size()-1);
        hash+=1;
        Nodes node=list.get(hash).node;
        if(node!=null)
            for(int i=0;i<4;i++)
            {
                if(number==node.values[i])
                {
                    node.values[i]=-1;
                    for(int y=0;y<4;y++)
                        if(node.values[y]==-1&&y!=3)
                        {
                            node.values[y]=node.values[y+1];
                            node.values[y+1]=-1;
                        }
                    if(node.isEmpty())
                        checkEqual(node);
                    Thread t=new Thread(this);
                    t.start();
                    return;
                }

            }
        JOptionPane.showMessageDialog(null, "The number you want to delete doesn't found","Error",JOptionPane.ERROR_MESSAGE);
    }
    private void checkEqual(Nodes node)
    {
        for(int i=1;i<list.size();i++)
        {
            if(list.get(i).node!=null)
                if(list.get(i).node.equals(node))
                {
                    list.get(i).node=null;
                }
        }
    }
    /*private void Print()
    {
        System.out.println(list.get(0).globalDepth);
        for(int i=1;i<list.size();i++)
        {
            System.out.println(list.get(i).value);
            Nodes node=list.get(i).node;
            if(node!=null)
            {
                System.out.println("Nodes : ");
                for(int y=0;y<4;y++)
                    System.out.println(y+"- "+node.values[y]);
            }
            else
                System.out.println("No Nodes to print");
        }
    }*/
    private void Expand()
    {
        double power=list.get(0).globalDepth;
        double oldValue=Math.pow(2, power);
        double newValue=Math.pow(2, power+1);
        double total=newValue-oldValue;
        list.get(0).globalDepth+=1;
        while(total!=0)
        {
            String value=Integer.toBinaryString(list.size()-1);
            list.add(new Table(value));
            total--;
        }
        addLeadingZero();
    }
    private void checkPointers()
    {
        int hash;
        for(int i=1;i<list.size();i++)
        {
           if(list.get(i).node!=null)
            for(int y=0;y<4;y++)
            {
                if(list.get(i).node.values[y]!=-1)
                {
                    hash=list.get(i).node.values[y]%(list.size()-1);
                    hash+=1;
                    if(list.get(hash).node==null)
                        list.get(hash).node=list.get(i).node;
                }
            }
        }
    }
    private boolean find(int number)
    {
        int hash=number%(list.size()-1);
        hash+=1;
        if(list.get(hash).node!=null)
            for(int n : list.get(hash).node.values)
            {
                if(number==n)
                    return true;
            }
        return false;
    }
    private void addLeadingZero()
    {
        int count=list.get(0).globalDepth;
        String s="0";
        for(int i=1;i<list.size();i++)
        {
            while(count>list.get(i).value.length())
            {
                list.get(i).value=s+list.get(i).value;
            }
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imagePanel1 = new Login.customcontrols.ImagePanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        HashPanel = new bzu.hashing.PaintPanel();
        imagePanel2 = new Login.customcontrols.ImagePanel();
        txtInsert = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Extendable Hashing");

        javax.swing.GroupLayout HashPanelLayout = new javax.swing.GroupLayout(HashPanel);
        HashPanel.setLayout(HashPanelLayout);
        HashPanelLayout.setHorizontalGroup(
            HashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1369, Short.MAX_VALUE)
        );
        HashPanelLayout.setVerticalGroup(
            HashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 486, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(HashPanel);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtInsert.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtInsertKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout imagePanel2Layout = new javax.swing.GroupLayout(imagePanel2);
        imagePanel2.setLayout(imagePanel2Layout);
        imagePanel2Layout.setHorizontalGroup(
            imagePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagePanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(txtInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1019, Short.MAX_VALUE))
        );
        imagePanel2Layout.setVerticalGroup(
            imagePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagePanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtInsert, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout imagePanel1Layout = new javax.swing.GroupLayout(imagePanel1);
        imagePanel1.setLayout(imagePanel1Layout);
        imagePanel1Layout.setHorizontalGroup(
            imagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagePanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        imagePanel1Layout.setVerticalGroup(
            imagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagePanel1Layout.createSequentialGroup()
                .addComponent(imagePanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu1.setText("File");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("New");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Minimize");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Exit");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("About");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imagePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(imagePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        this.LastY=0;
        this.lastX=0;
      //  jProgressBar1.setValue(0);
        list=new ArrayList<Table>();
        firstAppend();
        graphList=null;
        nodeGraph=null;
        HashPanel.lPaint=graphList;
        HashPanel.nPaint=nodeGraph;
        HashPanel.paintHash();
        txtInsert.setText("");
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
            System.exit(0);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        JOptionPane.showMessageDialog(null, "Author:\nAhmad Hammad \n ID:1081443\n Mohammad Yeaaqbi \n 1090068","About",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void txtInsertKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInsertKeyTyped
                if(!Character.isDigit(evt.getKeyChar()))
                {
                    evt.consume();
                }
    }//GEN-LAST:event_txtInsertKeyTyped

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
this.setState(this.ICONIFIED);        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem4ActionPerformed
    private void firstAppend()
    {
        list.add(new Table(null, 2));
        list.add(new Table("00"));
        list.add(new Table("01"));
        list.add(new Table("10"));
        list.add(new Table("11"));
    }
    private void prepareForPaint()
    {
        Nodes node;
        int xNode;
        LinePaint line;
        int width=100;//Rectangle
        //int height=25;//Rectangle
        int x=150,y=100;
        int squareDim=27;
        int yNode = y,yHash = y;
        graphList=new ArrayList<LinePaint>();
        nodeGraph=new ArrayList<NodePaint>();
        NodePaint nPaint;
        for(int i=0;i<list.size();i++)
            graphList.add(new LinePaint());
        graphList.get(0).globalDepth=list.get(0).globalDepth+"";
        graphList.get(0).x1=x;
        graphList.get(0).y1=y-20;
        for(int i=1;i<list.size();i++)
        {
            node=list.get(i).node;
            graphList.get(i).index=list.get(i).value;
            graphList.get(i).x1=x;
            graphList.get(i).y1=yHash;
            graphList.get(i).current=i;
            if(node!=null)
            {
                line=graphList.get(i);
                if(line.x2==-1&&line.y2==-1)
                {
                    nPaint=new NodePaint();
                    //xNode=line.x1+200;old
                    xNode=line.x1+400;
                    nPaint.localDepth=node.localDepth+"";
                    for(int count=0;count<4;count++)
                    {
                        nPaint.values[count]=node.values[count];
                        nPaint.x[count]=xNode;
                        xNode+=(squareDim);
                    }                                       
                    //nPaint.y=y;                                        
                    nPaint.y=yNode;         
                    LastNode=nPaint.y;
                    nodeGraph.add(nPaint);
                    //find equal node
                    graphList.get(i).x2=line.x1+200;
                    //graphList.get(i).y2=y;old
                    graphList.get(i).y2=yNode;
                    //findEqual(node, i+1, x+200, y);old
                    findEqual(node, i+1, x+200, yNode);
                }
             }
               // y+=(width/2);//old one
            yNode+=(width/2);
               // y+=(width/3)-8;
            yHash+=(width/3)-8;
        }
        lastX=graphList.get(graphList.size()-1).x1;
        LastY=graphList.get(graphList.size()-1).y1;
        
    }
    private void findEqual(Nodes node,int current,int x2,int y2)
    {
        LinePaint line;
        for(int i=current;i<list.size();i++)
        {
            if(list.get(i).node!=null)
                if(list.get(i).node.equals(node))
                {
                    line=new LinePaint();
                    line.x2=x2;
                    line.y2=y2;
                    line.current=i;
                    graphList.set(i, line);
                }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run()
            {
                new MainFrame().setVisible(true);
            }
        });
    }
      public void fullScreen(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Window win = new Window(this);//add jframe to window component
    win.setBackground(Color.black);
    this.imagePanel1.setBackground(Color.black);
     this.setBackground(Color.black);
      win.add(this.jMenuBar1,BorderLayout.NORTH);
       
         win.add(this.imagePanel1,BorderLayout.CENTER);
        //this.imagePanel1.setImage(new ImageIcon(this.getClass().getResource("BACKF.jpg")).getImage());
              this.imagePanel2.setImage(new ImageIcon(this.getClass().getResource("BACKt.jpg")).getImage());
                this.HashPanel.setImage(new ImageIcon(this.getClass().getResource("BACKF.jpg")).getImage());
        win.validate();
        
        gs.setFullScreenWindow(win);
 
             
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private bzu.hashing.PaintPanel HashPanel;
    private Login.customcontrols.ImagePanel imagePanel1;
    private Login.customcontrols.ImagePanel imagePanel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtInsert;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run()
    {
        prepareForPaint();
        HashPanel.lPaint=graphList;
        HashPanel.nPaint=nodeGraph;
       // jProgressBar1.setValue(0);
        //HashPanel.bar=jProgressBar1;
        HashPanel.New=this.New;
        Dimension d=new Dimension();
        d.width=lastX;
        d.height=LastNode+(int)(LastNode*0.1);
        HashPanel.setPreferredSize(d);
   
        HashPanel.revalidate();
        HashPanel.paintHash();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
     	if(e.getSource()==t6){
			System.exit(0);
		}
        if(e.getSource()==t1){
			
		
        
         if(!"".equals(txtInsert.getText().trim()))
        {
            int insert=Integer.parseInt(txtInsert.getText());
            txtInsert.setText("");
            Insert(insert);
            //Print();
            Thread t=new Thread(this);
            t.start();
        }
        else
            JOptionPane.showMessageDialog(null, "Please fill field","Error",JOptionPane.ERROR_MESSAGE);
        }
         if(e.getSource()==t2){
        
                if(!"".equals(txtInsert.getText().trim()))
        {
            int number=Integer.parseInt(txtInsert.getText());
            Delete(number);
        }
        else
            JOptionPane.showMessageDialog(null, "Please fill field","Error",JOptionPane.ERROR_MESSAGE);
         }
         
         if(e.getSource()==t3){
          JFileChooser chooser=new JFileChooser(".");
        int approval=chooser.showOpenDialog(null);
        String fName;
        if(approval==JFileChooser.APPROVE_OPTION)
        {
            fName=chooser.getSelectedFile().getAbsolutePath();
            readFromFile(fName);
        }
    }
         if(e.getSource()==t4){
             
              OutputFrame out=new OutputFrame();
        out.lst=this.list;
        out.Print();
        out.show();
             
         }
         
         
        if(e.getSource()==t5){    
                 this.LastY=0;
        this.lastX=0;
          LastNode=0;
        list=new ArrayList<Table>();
        firstAppend();
        graphList=null;
        nodeGraph=null;
        HashPanel.lPaint=graphList;
        HashPanel.nPaint=nodeGraph;
        HashPanel.paintHash();

        txtInsert.setText("");
    }
    }

   
    
}