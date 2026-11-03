import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public JButton playButton;

    public MenuPanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(new Color(110, 195, 205));
        setLayout(null);

        playButton = new JButton("GIOCA");
        playButton.setBounds(width / 2 - 100, height / 2 + 40, 200, 50);
        playButton.setFont(new Font("Arial Black", Font.BOLD, 18));
        playButton.setBackground(new Color(245, 190, 50));
        playButton.setForeground(Color.DARK_GRAY);
        playButton.setFocusPainted(false);
        playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        add(playButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setFont(new Font("Arial Black", Font.BOLD, 38));
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.drawString("FLAPPY BIRD", 62, 202);

        g2d.setColor(new Color(245, 190, 50));
        g2d.drawString("FLAPPY BIRD", 60, 200);
    }
}