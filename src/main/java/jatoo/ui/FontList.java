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

import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

/**
 * A list ({@link JList}) component that displays a list of fonts ({@link Font})
 * and allows the user to select one.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 2.0, August 25, 2014
 */
@SuppressWarnings("serial")
public class FontList extends JList<Font> {

  private final DefaultListModel<Font> model;

  public FontList() {
    this(true);
  }

  public FontList(final boolean addSystemFonts) {

    setModel(model = new DefaultListModel<>());

    setCellRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setText(((Font) value).getName());
        return this;
      }
    });

    if (addSystemFonts) {

      String[] fontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

      for (String fontFamilyName : fontFamilyNames) {
        addFont(new Font(fontFamilyName, Font.PLAIN, 1));
      }
    }

    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {

        boolean found = selectFontStartingWith(e.getKeyChar(), getSelectedIndex() + 1);

        if (!found) {
          selectFontStartingWith(e.getKeyChar(), 0);
        }
      }
    });
  }

  public final void addFont(final Font font) {
    model.addElement(font);
  }

  public final void addFont(final File file) throws FontFormatException, IOException {
    addFont(Font.createFont(Font.TRUETYPE_FONT, file));
  }

  public Font getSelectedFont() {
    return super.getSelectedValue();
  }

  private boolean selectFontStartingWith(char c, int startingIndex) {

    String prefix = String.valueOf(c).toLowerCase();

    for (int i = startingIndex; i < getModel().getSize(); i++) {

      Font font = (Font) model.getElementAt(i);

      boolean b1 = font.getName() != null && font.getName().toLowerCase().startsWith(prefix);
      boolean b2 = font.getFamily() != null && font.getFamily().toLowerCase().startsWith(prefix);

      if (b1 || b2) {
        setSelectedIndex(i);
        ensureIndexIsVisible(i);
        return true;
      }
    }

    return false;
  }

}
