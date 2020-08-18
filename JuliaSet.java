import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class JuliaSet extends JPanel {
  static final int MAX_ITERATIONS = 300;
  static final double ZOOM = 1.0;
  static final double CX = -1.2;
  static final double CY = 0.156;
  static final double MOVE_X = 0;
  static final double MOVE_Y = 0;

  static final int WIDTH = 1200;
  static final int HEIGHT = 1000;

  BufferedImage juliaSetImage;

  public static void main(String[] args) {
    EventQueue.invokeLater(new JuliaSet()::initGUI);
  }

  public JuliaSet() {
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
  }

  private void initGUI() {
    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setTitle("Julia Set Explorer");
    f.setResizable(false);
    f.add(new JuliaSet(), BorderLayout.CENTER);
    f.pack();
    f.setLocationRelativeTo(null);
    f.setVisible(true);
  }

  private int juliaColor(int iterCount) {

    int color = 0b100010000000111100;
    int mask = 0b000000000000010101110111;
    int shiftMag = iterCount / 10;

    if (iterCount == MAX_ITERATIONS) return Color.BLACK.getRGB();

    return color | (mask << shiftMag);
  }

  void drawJuliaSet(Graphics g) {

    juliaSetImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        double zx = 1.5 * (x - WIDTH / 2) / (0.5 * ZOOM * WIDTH) + MOVE_X;
        double zy = (y - HEIGHT / 2) / (0.5 * ZOOM * HEIGHT) + MOVE_Y;
        int max_iter = MAX_ITERATIONS;
        while (zx * zx + zy * zy < 4 && max_iter > 0) {
          double tmp = zx * zx - zy * zy + CX;
          zy = 2.0 * zx * zy + CY;
          zx = tmp;
          max_iter--;
        }
        int pixelcolor = juliaColor(max_iter);
        juliaSetImage.setRGB(x, y, pixelcolor);
      }
    }
    g.drawImage(juliaSetImage, 0, 0, null);
  }

  @Override
  public void paintComponent(Graphics gg) {
    super.paintComponent(gg);
    Graphics2D g = (Graphics2D) gg;
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    drawJuliaSet(g);
  }
}
