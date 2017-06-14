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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class ImageFileListTest extends JPanel {

  public static void main(String[] args) throws Exception {
    new TestFrame(new ImageFileListTest());
  }

  public ImageFileListTest() throws Exception {

    final ImageFileList images = new ImageFileList();

    for (File file : new File("src\\test\\resources\\jatoo\\ui\\").listFiles()) {
      if (file.isFile()) {

        for (int i = 0; i < 5; i++) {

          File newFile = new File("target\\test-" + ImageFileListTest.class.getSimpleName(), (i + 1) + "-" + file.getName());
          newFile.getParentFile().mkdirs();

          Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

          images.addImage(newFile);
        }
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

    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

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

    setLayout(new BorderLayout(5, 5));
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    add(images, BorderLayout.CENTER);
    add(buttons, BorderLayout.NORTH);
  }

}
