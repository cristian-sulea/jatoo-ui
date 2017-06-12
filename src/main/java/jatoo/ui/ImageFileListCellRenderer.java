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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import jatoo.image.ImageUtils;

/**
 * The cell renderer for {@link ImageFileList} component.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 2.0-SNAPSHOT, June 12, 2017
 */
@SuppressWarnings("serial")
public class ImageFileListCellRenderer extends JComponent implements ListCellRenderer<File> {

  private final ImageFileList imageFileList;

  private Icon defaultIcon;

  private final JToggleButton button;

  public ImageFileListCellRenderer(final ImageFileList imageFileList) {

    this.imageFileList = imageFileList;

    button = new JToggleButton();
    button.setHorizontalTextPosition(SwingConstants.CENTER);
    button.setVerticalTextPosition(SwingConstants.BOTTOM);

    setLayout(new GridLayout());
    add(button);

    fireItemSpaceChanged();
    fireIconStyleChanged();
  }

  public void fireIconStyleChanged() {

    //
    // default icon image

    BufferedImage themeDefaultImage = UITheme.getImage(ImageFileListCellRenderer.class, "DefaultImage");

    BufferedImage defaultIconImage = ImageUtils.create(imageFileList.getIconSize(), imageFileList.getIconSize(), true);
    Graphics defaultImageGraphics = defaultIconImage.getGraphics();
    defaultImageGraphics.drawImage(themeDefaultImage, (imageFileList.getIconSize() - themeDefaultImage.getWidth()) / 2, (imageFileList.getIconSize() - themeDefaultImage.getHeight()) / 2, null);
    defaultImageGraphics.dispose();

    if (imageFileList.isIconShadow()) {
      defaultIconImage = ImageUtils.addShadow(defaultIconImage);
    }

    //
    // set the new values

    this.defaultIcon = new ImageIcon(defaultIconImage);

    //
    // set dummy text and icon for the initial preferred size

    button.setText("defaultText");
    button.setIcon(defaultIcon);

    setPreferredSize(null);
    setPreferredSize(getPreferredSize());
  }

  public void fireItemSpaceChanged() {
    setBorder(BorderFactory.createEmptyBorder(imageFileList.getItemSpace(), imageFileList.getItemSpace(), 0, 0));
  }

  @Override
  public Component getListCellRendererComponent(JList<? extends File> list, File file, int index, boolean isSelected, boolean cellHasFocus) {

    //
    // text

    button.setText(file.getName());
    list.setToolTipText(file.getAbsolutePath());

    //
    // icon

    Icon icon = imageFileList.getIcon(file);

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
