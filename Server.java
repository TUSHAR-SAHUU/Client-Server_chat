package serverAndClient;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.FontUIResource;





class Server extends JFrame{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // for GUI
    private JLabel heading = new JLabel("Server Side");
    private JTextArea msgArea = new JTextArea();
    private JTextField msgField = new JTextField();
    private FontUIResource font = new FontUIResource("Roboto",Font.PLAIN, 20);
    
   
    public Server(){
        try {
            server = new ServerSocket(2346);
            System.out.println("connection is ready to connect....");
            System.out.println("waiting .....");
                    
            socket=server.accept();
          
            br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out= new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvent();
            startReading();
          //  startWriting();
           

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    
    private void handleEvent() {
        msgField.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
               // System.out.println("Key released"+ e.getKeyCode());
               if(e.getKeyCode()==10){
                 //  System.out.println("enter is pressed");
                 String contentToSent = msgField.getText();
                 msgArea.append("me : "+contentToSent+ "\n");
                 out.println(contentToSent);
                 out.flush();
                 msgField.setText("");
                 msgField.requestFocus();
               }
            }});
    }
    
    public void createGUI(){

        this.setTitle("Server End");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         // coding for component
        heading.setFont(font);
        msgArea.setFont(font);
        msgField.setFont(font);
      //  heading.setIcon(new ImageIcon("ChatImg.jpeg"));
        heading.setIcon(new ImageIcon("ChatImg.png.jpeg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        msgArea.setEditable(false);
         // setting frame layout
        this.setLayout(new BorderLayout());
         // adding the component to frame
         this.add(heading,BorderLayout.NORTH);
         JScrollPane scrollPane=new JScrollPane(msgArea);
         scrollPane.setAutoscrolls(true);
       
         this.add(scrollPane,BorderLayout.CENTER);
         this.add(msgField,BorderLayout.SOUTH);





        this.setVisible(true);

    }
    
    
     public void startReading(){

        Runnable r1=()->{

            try {
                System.out.println("reader started...");
                
                while(!socket.isClosed()){
                    String msg=br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("client ended the chat");
                        JOptionPane.showMessageDialog(this, "Client terminated");
                        msgField.setEnabled(false);
                        socket.close();
                        break;
                    }

                   // System.out.println("server: "+ msg);
                    msgArea.append("Client : "+msg+"\n");

                }
            } catch (Exception e) {
                System.out.println("connection exits");
            }
        };

        new Thread(r1).start();
    
     }
     public void startWriting() {

        System.out.println("writer started......"); 

        Runnable r2=()->{
            
           try {
            while(!socket.isClosed()){
            BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
            String content=br1.readLine();
            out.write(content);
            out.flush();
            if(content.equals("exit")){
                socket.close();
                break;

            }
           } 
        }catch (Exception e) {
           System.out.println("connection closed");
        }
            

        };

        new Thread(r2).start();
    }   
     
     
     
     
  public static void main(String[] args) {
      System.out.println("This is server.....");
      new Server();
  }
}
