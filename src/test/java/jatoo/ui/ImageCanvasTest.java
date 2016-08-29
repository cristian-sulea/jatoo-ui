package jatoo.ui;

import jatoo.image.ImageUtils;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ImageCanvasTest {

  public static void main(String[] args) throws Exception {

    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
    System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "debug");
    
    BufferedImage image = ImageUtils.create(400, 300, false);

//    ImageCanvas imageCanvas = new ImageCanvas(image);
//    imageCanvas.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));

    final ImageViewer imageCanvas = new ImageViewer(image);
//    imageCanvas.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));

    final JFrame frame = new JFrame(ImageCanvasTest.class.getSimpleName());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.getContentPane().setLayout(new GridLayout());
    frame.getContentPane().add(imageCanvas);

     frame.setSize(100, 150);
//    frame.pack();
    frame.setLocation(700, 600);

//    imageCanvas.setZoom(100);
    frame.setVisible(true);
    
    Thread.sleep(2000);
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
//          Thread.sleep(2000);
          System.out.println("zoom");
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
