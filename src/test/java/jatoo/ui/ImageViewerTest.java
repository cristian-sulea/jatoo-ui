/*
 * Copyright (C) Cristian Sulea ( http://cristian.sulea.net )
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jatoo.ui;

import jatoo.image.ImageUtils;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ImageViewerTest {

  public static void main(String[] args) throws Exception {

    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
    System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "trace");
    
    BufferedImage image = ImageUtils.read(new File("src\\test\\resources\\jatoo\\ui\\").listFiles()[0]);

//    ImageCanvas imageCanvas = new ImageCanvas(image);
//    imageCanvas.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));

//    image = ImageUtils.create("Loading...", null, Color.DARK_GRAY, Color.LIGHT_GRAY, true);
    final ImageViewerV4 imageViewer = new ImageViewerV4(image);
//    imageCanvas.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));

    final JFrame frame = new JFrame(ImageViewerTest.class.getSimpleName());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.getContentPane().setLayout(new GridLayout());
    frame.getContentPane().add(imageViewer);

     frame.setSize(800, 600);
//    frame.pack();
    frame.setLocation(650, 700);

//    imageViewer.setZoom(100);
    frame.setVisible(true);
    
    Thread.sleep(2000);
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
//          System.out.println("zoom");
//          imageViewer.xxx();
//          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//          SwingUtilities.updateComponentTreeUI(frame);
//          frame.pack();
//          imageViewer.setZoom(0);
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
