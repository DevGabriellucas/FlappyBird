import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    int LarguraBorda = 360;
    int AlturaBorda = 640;

    //IMAGEM
    Image birdImage;
    Image backgroundImage;
    Image bottomPipeImage;
    Image topPipeImage;

    //Passaro

    int birdX = LarguraBorda / 8;
    int birdY = AlturaBorda / 2;
    int birdWidth = 34;
    int birdHeight = 24;
    

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
        this.img = img;
        }
    }

    //Canos

    int PipeX = LarguraBorda;
    int PipeY = 0;
    int PipeWidth = 64;
    int PipeHeight = 512;

    class Pipe {
        int x = PipeX;
        int y = PipeY;
        int width = PipeWidth;
        int height = PipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
        this.img = img;
        }
    }



    //Logica do jogo
    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver = false;

    double counter = 0;
    int recorde = 0;
    Path recordePath = Path.of("recorde.txt");

    FlappyBird(){
        setPreferredSize(new Dimension(LarguraBorda, AlturaBorda));
        setFocusable(true);
        addKeyListener(this);
        carregarRecorde();
        
        backgroundImage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        bird = new Bird(birdImage);
        pipes = new ArrayList<Pipe>();

        
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();
            gameLoop = new Timer (1000/60, this);
            gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void placePipes () {
        int randomPypeY = (int) (PipeY - PipeHeight / 4 - Math.random() * (PipeHeight / 2));
        int openingSpace = AlturaBorda / 4;
        Pipe topPipe = new Pipe(topPipeImage);
        topPipe.y = randomPypeY;
        pipes.add(topPipe);
        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = topPipe.y + PipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void draw(Graphics g) {

        g.drawImage(backgroundImage, 0, 0, LarguraBorda, AlturaBorda, null);

        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null); 

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        String scoreText = "" + (int) counter;
        FontMetrics scoreMetrics = g.getFontMetrics();
        int scoreX = (LarguraBorda - scoreMetrics.stringWidth(scoreText)) / 2;
        int scoreY = 70;
        g.drawString(scoreText, scoreX, scoreY);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics gameOverMetrics = g.getFontMetrics();
            String gameOverText = "Game Over";
            int gameOverX = (LarguraBorda - gameOverMetrics.stringWidth(gameOverText)) / 2;
            int gameOverY = AlturaBorda / 2;
            g.drawString(gameOverText, gameOverX, gameOverY);

            g.setFont(new Font("Arial", Font.BOLD, 32));
            FontMetrics recordeMetrics = g.getFontMetrics();
            String recordeText = "" + recorde;
            int recordeX = (LarguraBorda - recordeMetrics.stringWidth(recordeText)) / 2;
            int recordeY = gameOverY + 45;
            g.drawString(recordeText, recordeX, recordeY);
        }
    }

    public void move() {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for (int i = 0; i < pipes.size(); i++) {



            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && pipe.x + pipe.width < bird.x) {
                pipe.passed = true;
                counter += 0.5;
                atualizarRecorde();
            }   

            if(collision(bird, pipe)){
            gameOver = true;
            }
        }

        if(bird.y > AlturaBorda) {
            gameOver = true;
        }

    }

    public void carregarRecorde() {
        try {
            if (Files.exists(recordePath)) {
                String texto = Files.readString(recordePath).trim();
                if (!texto.isEmpty()) {
                    recorde = Integer.parseInt(texto);
                }
            }
        } catch (IOException | NumberFormatException e) {
            recorde = 0;
        }
    }

    public void atualizarRecorde() {
        int pontuacaoAtual = (int) counter;

        if (pontuacaoAtual > recorde) {
            recorde = pontuacaoAtual;
            salvarRecorde();
        }
    }

    public void salvarRecorde() {
        try {
            Files.writeString(recordePath, "" + recorde);
        } catch (IOException e) {
            System.out.println("Nao foi possivel salvar o recorde.");
        }
    }

    public boolean collision (Bird a, Pipe b) {
        return a.x < b.x + b.width &&
        a.x + a.width > b.x &&      
        a.y < b.y + b.height &&
        a.y + a.height > b.y;        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if(gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -10;
        }

        if(gameOver){
            bird.x = birdX;
            bird.y = birdY;
            pipes.clear();
            counter = 0;
            gameOver = false;
            placePipesTimer.start();
            gameLoop.start();   
        }
    }


        @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
