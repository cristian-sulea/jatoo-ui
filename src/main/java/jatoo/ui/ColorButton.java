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
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;

/**
 * A button created to allow a user to manipulate and select a color (launching
 * a {@link JColorChooser}).
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 2.0, August 25, 2014
 */
@SuppressWarnings("serial")
public class ColorButton extends Button {

  public static final Color DEFAULT_SELECTED_COLOR = Color.BLACK;

  private final int iconWidth;
  private final int iconHeight;

  private Color selectedColor;

  public ColorButton() {
    this(16, 16, null);
  }

  public ColorButton(final Color initialColor) {
    this(16, 16, initialColor);
  }

  public ColorButton(final int iconWidth, final int iconHeight, final Color initialColor) {

    this.iconWidth = iconWidth;
    this.iconHeight = iconHeight;

    setColor(initialColor);

    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setColor(JColorChooser.showDialog(ColorButton.this.getTopLevelAncestor(), "Color", ColorButton.this.selectedColor));
      }
    });
  }

  /**
   * Returns the selected color.
   * 
   * @return the selected color.
   */
  public final Color getColor() {
    return selectedColor;
  }

  /**
   * Programmatically sets the selected color.
   * 
   * @param color
   *          the new value of the selected color
   */
  public final void setColor(Color color) {

    if (color == null) {
      color = DEFAULT_SELECTED_COLOR;
    }

    BufferedImage image = ImageUtils.create(iconWidth, iconHeight, false);

    Graphics2D g = image.createGraphics();
    g.setColor(color);
    g.fillRect(0, 0, image.getWidth(), image.getHeight());
    g.setColor(color.darker());
    g.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);
    g.dispose();

    setIcon(new ImageIcon(image));

    Color oldValue = this.selectedColor;
    Color newValue = color;

    this.selectedColor = color;

    firePropertyChange("selectedColor", oldValue, newValue);
  }

  public void addSelectedColorPropertyChangeListener(PropertyChangeListener listener) {
    addPropertyChangeListener("selectedColor", listener);
  }

}
