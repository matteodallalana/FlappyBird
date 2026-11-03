import javax.swing.*;

public class FlappyBird {
    public static void main(String[] args){
        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();

        frame.setVisible(true);
        gamePanel.requestFocus();
    }
}
