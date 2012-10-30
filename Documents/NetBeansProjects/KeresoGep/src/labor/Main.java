

package labor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import java.util.List;

public class Main extends Frame {
   
    final double d = 0.88;
   
    int sorok = 0;
    int elek = 0;
   
    TextField tfield;
    TextArea tarea;
   
    HashMap<Integer, String> linkek;
   
    Integer[][] szomsz; //szomszédossági mátrix
   // double[] pr; //pagerank
   HashMap<Integer, Double> pr; // Pagerank egy hashmap, hogy rendezéskor ne vesszen el az ID
   
   Main()
   {
       linkek = new HashMap<>();
       pr = new HashMap<>();
       
       Label label0 = new Label("Írd be a keresett nevét");
       
        tfield = new TextField("");
        
        tarea = new TextArea("");
       
        
       Button startButton = new Button("submit");
       
       Label label3 = new Label("");
       
       this.setLayout(new GridLayout(0,1));
       
       Panel p1 = new Panel();
       
       p1.setLayout(new GridLayout(0,1));
       
       p1.add(label0);
       
       p1.add(tfield); //beviteli mező, "top_depth.txt" fog rendesen futni
     
        p1.add(startButton); //ezzel indul el
       
        this.add(p1);
        
       this.add(tarea);
       
       this.setBounds(300,300, 300, 600);
   
       
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
       
           
           
           //A page rank mátrix egy egydimenziós tömb, hashmap ben implementálva
           
           for(int ii = 1; ii<=sorok; ii++) //inicializálás, 1től 500ig, és sorok == 500
           {
               double ti = 1.0;
               ti = ti/sorok;
                pr.put(ii, ti); 
           }
          
           for(int kk = 0; kk<100; kk++)  //rank számolása 100as mélységre
           {
                   
                    for(int ii = 1; ii<=sorok; ii++)// 1től 500ig, és sorok == 500
                    {
                       //N(j) számolása
                        int nj = 0;    
                        for(int ni = 0; ni<sorok; ni++)
                        { nj += szomsz[ii-1][ni]; } //ii-1, mert a tömb 0tól indul
                       
                        double szumma = 0;
                        double tempsum = 0;
                       
                        for(int jj = 1; jj<=sorok; jj++) //jj mint ii ben: 1től 500ig, és sorok==500
                        {
                             if(szomsz[jj-1][ii-1]==1) szumma += pr.get(jj) /nj;
                            
                        }
                       
                        //új pagerank
                       
                        double sum = d*szumma + (1.0-d)/sorok;
                        pr.put(ii, sum); //az ii indexű elembe eltárolja
                        
                       
                    }
                   
                     
           }
           
           
         //Az IDket kitesszük egy Listbe, majd sorbarendezzük őket a hozzátartozó PR alapján  
             List<Integer> idByPR = new ArrayList<>();
             idByPR.addAll(pr.keySet()); 
             
            Collections.sort(idByPR, new Comparator<Integer>() {
 
            @Override
        public int compare(Integer o1, Integer o2) {
            
                
                 if(pr.get(o1)<pr.get(o2)) return  1;
             //    if(pr.get(o1)==pr.get(o2)) return 0;
                 if(pr.get(o1)<pr.get(o2)) return -1;
                 
                 return 0;
        }
    });

           
           
   //10 legjobb találat
           tarea.setText("");
           for(int i=0; i<10; i++)
           {
               int ti = idByPR.get(i);
               
             tarea.append( ti + "\t" + linkek.get(ti) + "\t" + pr.get(ti) + "\n");
           }
   }
    
}
    
    

