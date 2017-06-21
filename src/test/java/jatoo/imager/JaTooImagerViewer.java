package jatoo.imager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import jatoo.image.ImageUtils;
import jatoo.ui.ImageCanvas;
import jatoo.ui.UIUtils;

@SuppressWarnings("serial")
public class JaTooImagerViewer extends JFrame {

  static {
    UIUtils.setSystemLookAndFeel();
  }

  public static void main(String[] args) throws Exception {
    new JaTooImagerViewer(new File("src\\test\\resources\\image.jpg"));

    // System.out.println(Toolkit.getDefaultToolkit().getScreenSize());
    //
    // System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth());
    // System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[1].getDisplayMode().getWidth());
    //
    // BufferedImage image = ImageUtils.read("d:\\IMG_1670.jpg");
    // image = ImageUtils.resizeToFit(image, 1920);
    // ImageUtils.writeJPEG(image, new File("d:\\IMG_1670_2.jpg"), 5);
  }

  private ImageCanvas canvas = new ImageCanvas();

  private List<File> files = new ArrayList<>();

  public JaTooImagerViewer(File file) throws Exception {

    final File folder;

    if (file.isDirectory()) {
      folder = file;
    } else {
      folder = file.getParentFile();
    }

    // disableDecorations();

    setContentPane(canvas);
    // getContentPane().add(canvas);

    UIUtils.setActionForEscapeKeyStroke(canvas, new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        JaTooImagerViewer.this.setVisible(false);
        JaTooImagerViewer.this.dispose();
      }
    });
    UIUtils.forwardDragAsMove(canvas, this);

    // canvas.setImage(ImageUtils.addShadow(ImageUtils.resizeToFit(ImageUtils.read(file), 350, 250)));
    // canvas.setImage(ImageUtils.addShadow(ImageUtils.read(file)));
    canvas.setImage(ImageUtils.read(file));

    // setIconImage(ImageUtils.resizeToFill(ImageUtils.read(file), 32));
    setTitle(file.getName());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(400, 300);
    setLocationRelativeTo(null);
    setLocation(400, 600);
    setVisible(true);

    canvas.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        // System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx");
      }
    });

    Thread.sleep(2000);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        disableDecorations();
      }
    });

    Thread.sleep(2000);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        enableDecorations();
      }
    });
  }

  private synchronized void disableDecorations() {

    boolean visible = isVisible();

    Point canvasLocation = null;
    Dimension canvasSize = null;
    if (visible) {
      canvasLocation = canvas.getLocationOnScreen();
      canvasSize = canvas.getSize();
    }

    if (isDisplayable()) {
      dispose();
    }

    setUndecorated(true);
    setBackground(new Color(1f, 1f, 1f, 0f));

    if (visible) {
      setLocation(canvasLocation);
      setSize(canvasSize);
      setVisible(true);
    }
  }

  private synchronized void enableDecorations() {

    boolean visible = isVisible();

    Point canvasLocationBefore = null;
    Dimension canvasSizeBefore = null;
    if (visible) {
      canvasLocationBefore = canvas.getLocationOnScreen();
      canvasSizeBefore = canvas.getSize();
    }

    if (isDisplayable()) {
      dispose();
    }

    setBackground(null);
    setUndecorated(false);

    if (visible) {

      setVisible(true);

      Point canvasLocationAfter = canvas.getLocationOnScreen();
      Dimension canvasSizeAfter = canvas.getSize();

      Point frameLocation = getLocationOnScreen();
      Dimension frameSize = getSize();

      setLocation(frameLocation.x - (canvasLocationAfter.x - canvasLocationBefore.x), frameLocation.y - (canvasLocationAfter.y - canvasLocationBefore.y));
      setSize(frameSize.width + (canvasSizeBefore.width - canvasSizeAfter.width), frameSize.height + (canvasSizeBefore.height - canvasSizeAfter.height));
    }
  }

}
