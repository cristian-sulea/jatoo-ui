/*
 * Copyright (C) Cristian Sulea ( http://cristian.sulea.net )
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
