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

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ImageFileListTest {

  public static void main(String[] args) throws Exception {

//    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    final ImageFileList images = new ImageFileList();

    for (File file : new File("c:\\Users\\cristian.sulea\\Downloads\\xxx\\").listFiles()) {
      if (file.isFile()) {
        images.addImage(file);
      }
    }

    images.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {

          System.out.println("xxx: " + images.getSelectedImage());
        }
      }
    });

    images.addListMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() >= 2) {

          System.out.println("yyy: " + images.getSelectedImage());
        }
      }
    });

    final JPanel cp = new JPanel(new GridLayout());
    cp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    cp.add(images);

    final JFrame frame = new JFrame(ImageFileList.class.getSimpleName() + " Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(cp);
    frame.setSize(800, 600);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setLocation(600, 500);
    frame.setVisible(true);
  }

}
