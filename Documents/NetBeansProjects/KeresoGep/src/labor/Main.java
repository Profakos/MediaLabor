

package labor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends Frame {
   
    final double d = 0.88;
   
    int sorok = 0;
    int elek = 0;
   
    TextField tfield;
   
    HashMap<Integer, String> linkek;
   
    Integer[][] szomsz; //szomszédossági mátrix
    double[] pr; //pagerank
   
   Main()
   {
       linkek = new HashMap<Integer, String>();
       
       
       Label label0 = new Label("Ird be a file nevét");
       
        tfield = new TextField("");
       
       Label label1 = new Label("Indításhoz kattints a gombra");
       Button startButton = new Button("submit");
       
       Label label3 = new Label("");
       
       this.setLayout(new GridLayout(0,1));
       
       this.add(label0);
       
       this.add(tfield); //beviteli mező, "top_depth.txt" fog rendesen futni
       
       this.add(label1);
       
       this.add(startButton); //ezzel indul el
       
       this.setBounds(600,300, 300, 300);
   
       
       this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent arg0) {
            Window w = arg0.getWindow();
            w.dispose();
            System.exit(0); }  });
       
       
       this.pack();
       this.setVisible(true);
       
       startButton.addActionListener(
               
                    new ActionListener(){  
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                    makeRankFile();
                    }} );
               
       
       
   }
   
   public static void main(String[] args)
   {
   Main main = new Main();      
   }

   void makeRankFile()
   {
       linkek.clear();
       
       // File file = new File("//home//user//anyag//top_depth.txt");
       
       File file = new File("C://Users//Akos//Desktop//anyag//top_depth.txt");
        
           StringBuffer contents = new StringBuffer();
           BufferedReader reader = null;
   
           try {
               reader = new BufferedReader(new FileReader(file));
               String text = null;
   
               
               int state = 0;
               int counter = 0;
               
   
              olvas: while ((text = reader.readLine()) != null) {
                  
                   switch(state)
                   {
                   case 0:  
                   counter = Integer.parseInt(text);
                   sorok = counter;
                   
                   szomsz = new Integer[sorok][sorok];
                     //Kialakul a szomszédsági mátrix, ahol [i][j]ben 0 lesz ha nincs link i ből j be, és 1 ha igen
                   
                   for(int ii = 0; ii<sorok; ii++)
                   {
                       for(int jj = 0; jj<sorok; jj++)
                       {
                           szomsz[ii][jj] = 0;
                       }
                       
                   }
                   
                   state++;
                   
                   break;
                   
                   case 1:
                       
                       linkek.put(Integer.parseInt(text.split(" ")[0]), text.split(" ")[1]);
                       
                       counter--;
                       if(counter==0) state++;
                       
                       
                       break;
                   case 2:  
                       counter = Integer.parseInt(text);
                       elek = counter;
                       
                   
                       state++;
                       break;
                   case 3:
                       
                       int elso = Integer.parseInt(text.split(" ")[0]); //innen
                       int masodik = Integer.parseInt(text.split(" ")[1]); //ide
                       
                       szomsz[elso-1][masodik-1] = 1; //egyet ki kell vonni, mert 1től indult
                       
                       break;
                   }
                   
               }
                
               
           } catch (FileNotFoundException e) {
               e.printStackTrace();  
           } catch (IOException e) {
               e.printStackTrace();  
           } finally {
               try {
                   if (reader != null) {
                       reader.close();
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       
           
           
           pr = new double[sorok]; //page rank mátrix
           
           for(int ii = 0; ii<sorok; ii++) //inicializálás
           {
               double ti = 1.0;
               ti = ti/sorok;
                   pr[ii] = ti;
           }
          
           for(int kk = 0; kk<100; kk++)  //rank számolása 100as mélységre
           {
                   
                    for(int ii = 0; ii<sorok; ii++)
                    {
                       //N(j) számolása
                        int nj = 0;    
                        for(int ni = 0; ni<sorok; ni++){ nj += szomsz[ii][ni]; }
                       
                       
                       
                        double szumma = 0;
                        double tempsum = 0;
                       
                        for(int jj = 0; jj<sorok; jj++)
                        {
                             if(szomsz[jj][ii]==1) szumma += pr[jj]/nj;
                            
                        }
                       
                        //új pagerank
                       
                        pr[ii] = d*szumma + (1.0-d)/sorok;
                        
                       
                       
                    }
                   
                     
           }
   
           for(int i=1; i<=sorok; i++)
           {
               
                System.out.println( i + " " + linkek.get(i) + " " + pr[i-1]);
           }
   }
    
}
    
    

