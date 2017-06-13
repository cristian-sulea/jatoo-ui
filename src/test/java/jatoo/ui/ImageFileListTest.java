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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ImageFileListTest {

  @SuppressWarnings("serial")
  public static void main(String[] args) throws Exception {

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    final ImageFileList images = new ImageFileList();

    for (File file : new File("c:\\Users\\cristian.sulea\\Downloads\\xxx\\").listFiles()) {
      if (file.isFile()) {
        images.addImage(file);
        // break;
      }
    }

    images.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          if (images.getSelectedImage() != null) {
            System.out.println("xxx: " + images.getSelectedImage());
          }
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

    final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

    buttons.add(new JButton(new AbstractAction("-5 Icon Size") {
      public void actionPerformed(ActionEvent e) {
        images.setIconSize(images.getIconSize() - 5);
      }
    }));
    buttons.add(new JButton(new AbstractAction("+5 Icon Size") {
      public void actionPerformed(ActionEvent e) {
        images.setIconSize(images.getIconSize() + 5);
      }
    }));
    buttons.add(new JCheckBox(new AbstractAction("Drop Shadow") {
      public void actionPerformed(ActionEvent e) {
        images.setIconShadow(((JToggleButton) e.getSource()).isSelected());
      }
    }));

    buttons.add(new JButton(new AbstractAction("-1 Item Space") {
      public void actionPerformed(ActionEvent e) {
        images.setItemSpace(images.getItemSpace() - 5);
      }
    }));
    buttons.add(new JButton(new AbstractAction("+1 Item Space") {
      public void actionPerformed(ActionEvent e) {
        images.setItemSpace(images.getItemSpace() + 5);
      }
    }));

    final JPanel cp = new JPanel(new BorderLayout(5, 5));
    cp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    cp.add(images, BorderLayout.CENTER);
    cp.add(buttons, BorderLayout.NORTH);

    final JFrame frame = new JFrame(ImageFileList.class.getSimpleName() + " Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(cp);
    // frame.setSize(800, 600);
    frame.pack();
    frame.setLocationRelativeTo(null);
    // frame.setLocation(600, 500);
    frame.setVisible(true);
  }

}
