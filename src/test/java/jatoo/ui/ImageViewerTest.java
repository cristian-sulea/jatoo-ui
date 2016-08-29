package jatoo.ui;

import jatoo.image.ImageUtils;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ImageViewerTest {

  public static void main(String[] args) throws Exception {

    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
    System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "trace");
    
    BufferedImage image = ImageUtils.read(new File("c:\\Motorola-ATRIX-4G-Stock-Wallpapers-17.jpg"));

//    ImageCanvas imageCanvas = new ImageCanvas(image);
//    imageCanvas.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));

    final ImageViewer imageCanvas = new ImageViewer(image);
//    imageCanvas.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));

    final JFrame frame = new JFrame(ImageViewerTest.class.getSimpleName());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.getContentPane().setLayout(new GridLayout());
    frame.getContentPane().add(imageCanvas);

     frame.setSize(300, 200);
//    frame.pack();
    frame.setLocation(650, 700);

    imageCanvas.setZoom(100);
    frame.setVisible(true);
    
    Thread.sleep(5000);
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
//          Thread.sleep(2000);
          System.out.println("zoom");
//          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//          SwingUtilities.updateComponentTreeUI(frame);
//          frame.pack();
//          imageCanvas.setZoom(100);
//          frame.invalidate();
//          frame.validate();
//          frame.revalidate();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

}
