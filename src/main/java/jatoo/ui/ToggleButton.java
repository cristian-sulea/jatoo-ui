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

import java.awt.Graphics;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;

/**
 * Improved {@link JToggleButton} with many new things like opacity.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.1, July 10, 2014
 */
@SuppressWarnings("serial")
public class ToggleButton extends JToggleButton implements AlphaComponent {

  /** The helper for opacity. */
  private final AlphaComponentHelper alphaHelper = new AlphaComponentHelper(this);

  /**
   * Creates an initially unselected toggle button without setting the text or
   * image.
   */
  public ToggleButton() {
    super();
  }

  /**
   * Creates an unselected toggle button with the specified text.
   * 
   * @param text
   *          the string displayed on the toggle button
   */
  public ToggleButton(String text) {
    super(text);
  }

  /**
   * Creates a toggle button with the specified text and selection state.
   * 
   * @param text
   *          the string displayed on the toggle button
   * @param selected
   *          if true, the button is initially selected; otherwise, the button
   *          is initially unselected
   */
  public ToggleButton(String text, boolean selected) {
    super(text, selected);
  }

  /**
   * Creates an initially unselected toggle button with the specified image but
   * no text.
   * 
   * @param icon
   *          the image that the button should display
   */
  public ToggleButton(Icon icon) {
    super(icon);
  }

  /**
   * Creates an initially unselected toggle button with the specified icon and
   * selected icon but no text.
   * 
   * @param icon
   *          the image that the button should display
   * @param selectedIcon
   *          the icon used as the "selected" image
   */
  public ToggleButton(Icon icon, Icon selectedIcon) {
    super(icon);
    setSelectedIcon(selectedIcon);
  }

  /**
   * Creates a toggle button with the specified image and selection state, but
   * no text.
   * 
   * @param icon
   *          the image that the button should display
   * @param selected
   *          if true, the button is initially selected; otherwise, the button
   *          is initially unselected
   */
  public ToggleButton(Icon icon, boolean selected) {
    super(icon, selected);
  }

  /**
   * Creates a toggle button with the specified image and selection state, but
   * no text.
   * 
   * @param icon
   *          the image that the button should display
   * @param selected
   *          if true, the button is initially selected; otherwise, the button
   *          is initially unselected
   */
  public ToggleButton(String text, Icon icon) {
    super(text, icon);
  }

  /**
   * Creates a toggle button with the specified text, image, and selection
   * state.
   * 
   * @param text
   *          the text of the toggle button
   * @param icon
   *          the image that the button should display
   * @param selected
   *          if true, the button is initially selected; otherwise, the button
   *          is initially unselected
   */
  public ToggleButton(String text, Icon icon, boolean selected) {
    super(text, icon, selected);
  }

  /**
   * Creates a toggle button where properties are taken from the Action
   * supplied.
   * 
   * @param action
   *          the {@link Action} used to specify the new button
   */
  public ToggleButton(Action action) {
    super(action);
  }

  @Override
  protected void paintComponent(Graphics g) {
    alphaHelper.paintComponent(g);
  }

  @Override
  protected void paintBorder(Graphics g) {
    alphaHelper.paintBorder(g);
  }

  @Override
  public boolean isOpaque() {
    return alphaHelper.isOpaque();
  }

  @Override
  public void setAlpha(float alpha) {
    alphaHelper.setAlpha(alpha);
  }

  @Override
  public void setAlpha(float alpha, boolean visibility) {
    alphaHelper.setAlpha(alpha, visibility);
  }

  @Override
  public void setPaintBackground(boolean paintBackground) {
    alphaHelper.setPaintBackground(paintBackground);
  }

  @Override
  public void superPaintComponent(Graphics g) {
    super.paintComponent(g);
  }

  @Override
  public void superPaintBorder(Graphics g) {
    super.paintBorder(g);
  }

}
