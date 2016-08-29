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

import jatoo.image.ImageThumbnails;
import jatoo.image.ImageUtils;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

/**
 * The cell renderer for {@link ImageFileList} component.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.1, July 31, 2014
 */
@SuppressWarnings("serial")
public class ImageFileListCellRenderer extends JComponent implements ListCellRenderer<File> {

  private final ImageThumbnails thumbnails = new ImageThumbnails();
  private final Map<File, Icon> cache = new HashMap<>();

  private final int iconSize;
  private final boolean addShadow;

  private final JToggleButton button;

  private final Icon defaultIcon;

  public ImageFileListCellRenderer(final int iconSize, final boolean addShadow) {

    this.iconSize = iconSize;
    this.addShadow = addShadow;

    //
    // default image

    BufferedImage themeDefaultImage = UITheme.getImage(ImageFileListCellRenderer.class, "DefaultImage");

    BufferedImage defaultImage = ImageUtils.create(iconSize, iconSize, true);
    Graphics defaultImageGraphics = defaultImage.getGraphics();
    defaultImageGraphics.drawImage(themeDefaultImage, (iconSize - themeDefaultImage.getWidth()) / 2, (iconSize - themeDefaultImage.getHeight()) / 2, null);
    defaultImageGraphics.dispose();

    if (addShadow) {
      defaultImage = ImageUtils.addShadow(defaultImage);
    }

    defaultIcon = new ImageIcon(defaultImage);

    //
    // button

    button = new JToggleButton();
    button.setHorizontalTextPosition(SwingConstants.CENTER);
    button.setVerticalTextPosition(SwingConstants.BOTTOM);

    //
    // set dummy text and icon for the initial preferred size

    button.setText("text");
    button.setIcon(defaultIcon);

    //
    // layout

    setLayout(new GridLayout());
    add(button);

    setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    setPreferredSize(getPreferredSize());
  }

  public void refreshImage(final ImageFileList list, final ImageFileListModel model, final File file) {
    refreshImages(list, model, Arrays.asList(file));
  }

  public void refreshImages(final ImageFileList list, final ImageFileListModel model, final List<File> files) {

    new Thread() {
      public void run() {

        for (File file : files) {

          BufferedImage image = thumbnails.get(file, iconSize, iconSize);

          if (image != null) {

            synchronized (cache) {

              if (addShadow) {
                cache.put(file, new ImageIcon(ImageUtils.addShadow(image)));
              } else {
                cache.put(file, new ImageIcon(image));
              }
            }

            model.fireContentsChanged();
          }
        }
      }
    }.start();
  }

  @Override
  public Component getListCellRendererComponent(JList<? extends File> list, File file, int index, boolean isSelected, boolean cellHasFocus) {

    //
    // text

    button.setText(file.getName());
    list.setToolTipText(file.getAbsolutePath());

    //
    // icon

    final Icon icon;

    synchronized (cache) {
      icon = cache.get(file);
    }

    if (icon != null) {
      button.setIcon(icon);
    } else {
      button.setIcon(defaultIcon);
    }

    //
    // selection

    button.setSelected(isSelected);

    //
    // here we go

    return this;
  }

}
