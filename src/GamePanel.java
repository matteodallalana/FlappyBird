import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 640;
    int boardHeight = 640;

    Bird bird;
    ArrayList<Pipe> pipes;

    int velocityY = 0;
    int velocityX = -4;
    int gravity = 1;
    double score = 0;

    boolean gameOver = false;
    boolean gameStarted = false;

    long gameOverTime = 0;

    Timer gameLoop;
    Timer placePipeTimer;

    MenuPanel menuPanel;
    JButton restartButton;

    public GamePanel(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(new Color(110, 195, 205));
        setFocusable(true);
        addKeyListener(this);
        setLayout(null);

        bird = new Bird(boardWidth / 8, boardHeight / 2, 34, 24);
        pipes = new ArrayList<>();

        // Menu iniziale
        menuPanel = new MenuPanel(boardWidth, boardHeight);
        menuPanel.setBounds(0, 0, boardWidth, boardHeight);
        menuPanel.playButton.addActionListener(e -> startGame());
        add(menuPanel);

        // Pulsante RICOMINCIA
        restartButton = new JButton("RICOMINCIA");
        restartButton.setBounds(boardWidth / 2 - 100, boardHeight / 2 + 40, 200, 50);
        restartButton.setFont(new Font("Arial Black", Font.BOLD, 16));
        restartButton.setBackground(new Color(245, 190, 50));
        restartButton.setForeground(Color.DARK_GRAY);
        restartButton.setFocusPainted(false);
        restartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        restartButton.setVisible(false);
        restartButton.addActionListener(e -> restartGame());
        add(restartButton);

        placePipeTimer = new Timer(1500, e -> placePipes());
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    private void startGame() {
        gameStarted = true;
        menuPanel.setVisible(false);
        placePipeTimer.start();
        requestFocusInWindow();
    }

    private void restartGame() {
        bird.y = boardHeight / 2;
        velocityY = 0;
        pipes.clear();
        score = 0;
        gameOver = false;
        restartButton.setVisible(false);
        placePipeTimer.start();
        requestFocusInWindow();
    }

    private void placePipes() {
        int pipeHeight = 512;
        int randomPipeY = (int)(-pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;

        pipes.add(new Pipe(boardWidth, randomPipeY, 64, pipeHeight, true));
        pipes.add(new Pipe(boardWidth, randomPipeY + pipeHeight + openingSpace, 64, pipeHeight, false));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (!gameStarted) return;
        draw(g);
    }

    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Disegno Uccello
        g2d.setColor(Color.orange);
        g2d.fillRect(bird.x, bird.y, bird.width, bird.height);

        // Disegno Tubi
        g2d.setColor(new Color(34, 177, 76));
        for(Pipe pipe : pipes){
            g2d.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        // Punteggio e Game Over
        g2d.setColor(Color.white);
        g2d.setFont(new Font("Arial Black", Font.BOLD, 32));

        if(gameOver){
            g2d.drawString("Game Over", 80, boardHeight / 2 - 60);

            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Punteggio: " + (int) score, 110, boardHeight / 2 - 15);

            restartButton.setVisible(true); // Mostra il pulsante
        } else {
            g2d.drawString(String.valueOf((int) score), 20, 45);
        }
    }

    public void move(){
        if (!gameStarted || gameOver) return;

        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passes && pipe.isTop && bird.x > pipe.x + pipe.width){
                score += 1;
                pipe.passes = true;
            }

            if(collision(bird, pipe)){
                gameOver = true;
                gameOverTime = System.currentTimeMillis(); // Registra il momento esatto del game over
            }
        }

        pipes.removeIf(pipe -> pipe.x + pipe.width < 0);

        if(bird.y > boardHeight){
            gameOver = true;
            gameOverTime = System.currentTimeMillis();
        }

        if(gameOver){
            placePipeTimer.stop();
        }
    }

    public boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            if (gameStarted && !gameOver) {
                velocityY = -9;
            } else if (gameOver) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - gameOverTime > 500) {
                    restartGame();
                }
            }
        }
    }

    @Override public void keyTyped(KeyEvent e){}
    @Override public void keyReleased(KeyEvent e){}
}