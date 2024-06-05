import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;

public class SnakeGame extends JPanel implements ActionListener {

    private final int width;
    private final int height;
    private static final int FRAME_RATE = 20;
    public SnakeGame(final int width, final int height) {
        super();
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
    }

    public void startGame(){
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE)
                    System.out.println("space pressed");
            }
        });

        // calls action performed method
        new Timer(1000 / FRAME_RATE, this).start();

    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.setColor(Color.WHITE);
        graphics.setFont(graphics.getFont().deriveFont(30F));
        int currentHeight = height/4;
        final var graphics2D = (Graphics2D) graphics;
        final var frc = graphics2D.getFontRenderContext();
        String message = "Snake Game\nPress space to play!";
        for(final var line : message.split("\n")){
            final var layout = new TextLayout(line, graphics.getFont(), frc);
            final var bounds = layout.getBounds();
            final var targetWidth = (float)(width - bounds.getWidth()) / 2;
            layout.draw(graphics2D, targetWidth, currentHeight);
            currentHeight += graphics.getFontMetrics().getHeight();
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        repaint();
    }
}
