import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener, MouseListener {
    private int birdY = 150, birdVelocity = 0;
    private ArrayList<Rectangle> pipes;
    private Timer timer;
    private boolean gameOver = false;
    private boolean gameStarted = false;
    private int gravity = 1;
    private JButton restartButton;
    private int score = 0;
    private boolean scored = false;

    public FlappyBird() {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setResizable(false);
        frame.setVisible(true);

        pipes = new ArrayList<>();
        addPipe(true);
        addPipe(true);
        addPipe(true);
        addPipe(true);

        timer = new Timer(20, this);
        timer.start();

        restartButton = new JButton("Reiniciar");
        restartButton.setBounds(150, 125, 100, 50);
        restartButton.addActionListener(e -> resetGame());
        restartButton.setVisible(false);
        this.setLayout(null);
        this.add(restartButton);

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        addMouseListener(this); // Adiciona o MouseListener
    }

    private void addPipe(boolean start) {
        int space = 100;
        int width = 40;
        int height = 25 + (int) (Math.random() * 150);

        if (start) {
            pipes.add(new Rectangle(400 + width + pipes.size() * 200, 300 - height, width, height));
            pipes.add(new Rectangle(400 + width + (pipes.size() - 1) * 200, 0, width, 300 - height - space));
        } else {
            pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x + 300, 300 - height, width, height));
            pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x, 0, width, 300 - height - space));
        }
    }

    private void resetGame() {
        birdY = 150;
        birdVelocity = 0;
        pipes.clear();
        addPipe(true);
        addPipe(true);
        addPipe(true);
        addPipe(true);
        gameOver = false;
        score = 0;
        scored = false;
        gameStarted = false;
        restartButton.setVisible(false);
        timer.start();
        requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, 400, 300);

        g.setColor(Color.white);
        g.fillRect(50, birdY, 10, 10);

        g.setColor(Color.gray);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 10, 20);

        if (gameOver) {
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.setFont(new Font("Arial", Font.PLAIN, 30));
            g.drawString("Score: " + score, 150, 200);
            restartButton.setVisible(true);
        }

        if (!gameStarted && !gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Pressione Espaço ou Clique para Começar", 50, 150);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameStarted) {
            return;
        }

        if (gameOver) {
            return;
        }

        birdY += birdVelocity;
        birdVelocity += gravity;

        for (int i = 0; i < pipes.size(); i++) {
            Rectangle pipe = pipes.get(i);
            pipe.x -= 5;
        }

        if (pipes.get(0).x + pipes.get(0).width < 0) {
            pipes.remove(0);
            pipes.remove(0);
            addPipe(false);
        }

        for (Rectangle pipe : pipes) {
            if (pipe.intersects(new Rectangle(50, birdY, 10, 10))) {
                gameOver = true;
                timer.stop();
            }
        }

        for (int i = 0; i < pipes.size(); i += 2) {
            Rectangle pipe = pipes.get(i);
            if (pipe.x + pipe.width < 50 && !scored) {
                score++;
                scored = true;
            }
            if (pipe.x + pipe.width >= 50) {
                scored = false;
            }
        }

        if (birdY > 300 || birdY < 0) {
            gameOver = true;
            timer.stop();
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameStarted) {
                gameStarted = true;
                timer.start();
            }
            birdVelocity = -10;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameStarted) {
            gameStarted = true;
            timer.start();
        }
        birdVelocity = -10;
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        new FlappyBird();
    }
}