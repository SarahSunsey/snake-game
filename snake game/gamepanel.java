import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.Random;
public class gamepanel extends JPanel implements ActionListener {
    static final int Screen_width= 600;
    static final int Screen_height= 600;
    static final int Unit_size= 25;
    static final int Game_units = (Screen_width*Screen_height)/Unit_size;
    static final int DELAY = 100;
    final int x[]= new int [Game_units];
    final int y[]= new int [Game_units];
    int bodyParts =4;
    int appleEATEN;
    int appleX;
    int appleY;
    char direction ='R';
    boolean running = false;
    Timer timer;
    Random random;
    gamepanel() {
       random = new Random();
       this.setPreferredSize(new Dimension(Screen_width,Screen_height));
       this.setBackground(Color.pink);
       this.setLayout(null); // Set layout manager to null
       this.setFocusable(true);
       this.addKeyListener(new MyKeyAdapter());
       StartGame();
    }
    
    
    public void StartGame(){
        newApple();
        running= true;
        timer=new Timer(DELAY,this);
        timer.start();
       
    }
    public void newApple(){
        appleX = random.nextInt((int)(Screen_width/Unit_size))*Unit_size;
        appleY= random.nextInt((int)(Screen_height/Unit_size))*Unit_size;

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Color startColor = new Color(0xF7D147);
        Color endColor = new Color(0xE7557C);

        // You can customize the gradient direction and positions as needed
        GradientPaint gradientPaint = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);

        // Set the paint for the Graphics2D object
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(gradientPaint);

        // Fill the entire panel with the gradient paint
        g2d.fillRect(0, 0, getWidth(), getHeight());
        draw(g);
    }
    public void draw(Graphics g){
        if(running){
        /*for(int i =0;i<Screen_height/Unit_size;i++){
            g.drawLine(i*Unit_size, 0, i*Unit_size, Screen_height);
            g.drawLine(0, i*Unit_size, Screen_width, i*Unit_size);

        }*/
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, Unit_size, Unit_size);
        for(int i=0; i<bodyParts;i++){
            if(i==0) {
                g.setColor(Color.BLUE);
                g.fillRect(x[i], y[i], Unit_size, Unit_size);
            }
            else{
                g.setColor(new Color(0x6319D1));
                //g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                 g.fillRect(x[i], y[i], Unit_size, Unit_size);
            }
        }
         g.setColor(Color.white);
        g.setFont(new Font("frogie", Font.PLAIN, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("SCORE :" + appleEATEN,(Screen_width-metrics.stringWidth("SCORE :" + appleEATEN))/2, g.getFont().getSize());
    }
    
    else{
        GameOver(g);
    }
    }
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
    
        switch (direction) {
            case 'U':
                y[0] = (y[0] - Unit_size + Screen_height) % Screen_height;
                break;
            case 'D':
                y[0] = (y[0] + Unit_size) % Screen_height;
                break;
            case 'L':
                x[0] = (x[0] - Unit_size + Screen_width) % Screen_width;
                break;
            case 'R':
                x[0] = (x[0] + Unit_size) % Screen_width;
                break;
        }
    }
    
    public void checkApple(){
        if((x[0]== appleX) && (y[0] == appleY) ){

           bodyParts++;
           appleEATEN++;
           newApple();
        }
    }
    public void checkCollitions(){
        //checks if head collides with body ??
        for(int i = bodyParts ; i>0;i--){
            if((x[0] == x[i]) && (y[0]==y[i])){
                running=false;
            }
        }
        //here we check if hea touches left border :P
        //left right top bottom
        if(x[0]<0 || x[0]>Screen_width || y[0]<0 || y[0]>Screen_height){
            running=false;
        }
        if(!running){
            timer.stop();
        }
        
    }
    public void GameOver(Graphics g) {
        // Game over text 
        g.setColor(Color.red);
        g.setFont(new Font("frogie", Font.PLAIN, 70));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (Screen_width - metrics1.stringWidth("GAME OVER")) / 2, Screen_height / 2);
        g.setColor(Color.red);
        g.setFont(new Font("frogie", Font.PLAIN, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("SCORE :" + appleEATEN, (Screen_width - metrics2.stringWidth("SCORE :" + appleEATEN)) / 2, g.getFont().getSize());
    
        // Remove existing components before adding the replay button
        removeAll();
        JButton replayButton = new JButton("Replay");
        replayButton.setFont(new Font("frogie", Font.PLAIN, 30));
        replayButton.setSize(150, 50);
        replayButton.setLocation((Screen_width - replayButton.getWidth()) / 2, Screen_height / 2 + 100);
    
        replayButton.addActionListener(e -> {
            resetGame(); // Call the method to reset the game
        });
    
        // Add the replay button to the panel
        add(replayButton);
        replayButton.setVisible(true);
    
        replayButton.requestFocusInWindow();
    }
    
    
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollitions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                if(direction != 'R'){
                    direction='L';
                }
                break;
                case KeyEvent.VK_RIGHT:
                if(direction != 'L'){
                    direction='R';
                }
                break;
                case KeyEvent.VK_UP:
                if(direction != 'D'){
                    direction='U';
                }
                break;

                case KeyEvent.VK_DOWN:
                if(direction != 'U'){
                    direction='D';
                }
                break;

            }
        }
        }
        public void resetGame() {
            bodyParts = 4;
            appleEATEN = 0;
            direction = 'R';
            running = false;
            timer.stop();
        
            // Reset the snake position
             for (int i = 0; i < bodyParts; i++) {
                x[i] = -i * Unit_size;
                y[i] = 0;
            }
        
            // Remove the replay button
            removeAll();
        
            // Restart the game
            StartGame();
        
            // Re-add the KeyListener
            this.addKeyListener(new MyKeyAdapter());
        
            // Request focus for the panel to ensure it receives key events
            this.requestFocusInWindow();
        
            repaint();
        }
        
    
        
}
