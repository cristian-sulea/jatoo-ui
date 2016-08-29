/*
 * Copyright (C) Cristian Sulea ( http://cristiansulea.entrust.ro )
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
