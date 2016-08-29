/*
 * Copyright (C) 2014 Cristian Sulea ( http://cristian.sulea.net )
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
