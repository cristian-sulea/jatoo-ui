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
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
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
 * @version 2.0-SNAPSHOT, June 13, 2017
 */
@SuppressWarnings("serial")
public class ImageFileListCellRenderer extends JComponent implements ListCellRenderer<File> {

  private final ImageFileList imageFileList;

  private Icon loadingIcon;
  private Icon errorIcon;

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

    Image baseIconImage = imageFileList.getResourcesImages().getImage("ImageFileList.baseIconImage.png");

    Image baseIconImageLoading = imageFileList.getResourcesImages().getImage("ImageFileList.baseIconImageLoading.png");
    Image baseIconImageError = imageFileList.getResourcesImages().getImage("ImageFileList.baseIconImageError.png");

    //
    // set the new values

    this.loadingIcon = createIcon(baseIconImage, baseIconImageLoading);
    this.errorIcon = createIcon(baseIconImage, baseIconImageError);

    //
    // set dummy text and loading icon for the initial preferred size

    button.setText("text");
    button.setIcon(loadingIcon);

    setPreferredSize(null);
    setPreferredSize(getPreferredSize());
  }

  public void fireItemSpaceChanged() {
    setBorder(BorderFactory.createEmptyBorder(imageFileList.getItemSpace(), imageFileList.getItemSpace(), 0, 0));

    setPreferredSize(null);
    setPreferredSize(getPreferredSize());
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

    if (icon == null) {
      button.setIcon(loadingIcon);
    }

    else {

      if (icon == ImageFileList.ICON_ERROR) {
        button.setIcon(errorIcon);
      }

      else {
        button.setIcon(icon);
      }
    }

    //
    // selection

    button.setSelected(isSelected);

    //
    // here we go

    return this;
  }

  private Icon createIcon(final Image baseIconImage, final Image baseIconImageXXX) {

    int iconWidth = imageFileList.getIconSize();
    int iconHeight = iconWidth;

    int baseWidth = baseIconImage.getWidth(null);
    int baseHeight = baseIconImage.getHeight(null);

    int xxxWidth = baseIconImageXXX.getWidth(null);
    int xxxHeight = baseIconImageXXX.getHeight(null);

    BufferedImage image = ImageUtils.create(iconWidth, iconHeight, true);
    Graphics defaultImageGraphics = image.getGraphics();
    defaultImageGraphics.drawImage(baseIconImage, (iconWidth - baseWidth) / 2, (iconHeight - baseHeight) / 2, null);
    defaultImageGraphics.drawImage(baseIconImageXXX, (iconWidth - baseWidth) / 2 + baseWidth - xxxWidth, (iconHeight - baseHeight) / 2 + baseHeight - xxxHeight, null);
    defaultImageGraphics.dispose();

    if (imageFileList.isIconShadow()) {
      image = ImageUtils.addShadow(image);
    }

    return new ImageIcon(image);
  }

}
