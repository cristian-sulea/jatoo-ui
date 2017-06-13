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

import java.awt.Color;
import java.awt.Graphics;

/**
 * Interface representing the support for opacity in UI components.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.0, July 10, 2014
 */
public interface AlphaComponent {

  /**
   * Sets the opacity of the alpha component.
   * <p>
   * The opacity value is in the range [0.0f .. 1.0f].
   * 
   * @param alpha
   *          the opacity level
   */
  void setAlpha(final float alpha);

  /**
   * Sets the opacity of the alpha component and updates the visibility (using
   * method {@link #setVisible(boolean)}).
   * <p>
   * The opacity value is in the range [0.0f .. 1.0f].
   * 
   * @param alpha
   *          the opacity level
   * @param visibility
   *          <code>true</code> to make the alpha component visible,
   *          <code>false</code> to make it invisible (using method
   *          {@link #setVisible(boolean)})
   */
  void setAlpha(final float alpha, final boolean visibility);

  /**
   * Enable or disable the paint of the background.
   * 
   * @param paintBackground
   *          <code>true</code> to faint the background, <code>false</code>
   *          otherwise
   */
  void setPaintBackground(boolean paintBackground);

  /**
   * Forwards a call to super#paintComponent(Graphics)
   * 
   * @param g
   *          the {@link Graphics} context in which to paint
   */
  void superPaintComponent(Graphics g);

  /**
   * Forwards a call to super#paintBorder(Graphics)
   * 
   * @param g
   *          the {@link Graphics} context in which to paint
   */
  void superPaintBorder(Graphics g);

  /**
   * Returns the current width of this component.
   * 
   * @return the current width of this component
   */
  int getWidth();

  /**
   * Returns the current height of this component.
   * 
   * @return the current height of this component
   */
  int getHeight();

  /**
   * Gets the background color of this component.
   * 
   * @return this component's background color; if this component does not have
   *         a background color, the background color of its parent is returned
   */
  Color getBackground();

  /**
   * Repaints this component.
   */
  void repaint();

  /**
   * Makes the component visible or invisible.
   * 
   * @param b
   *          <code>true</code> to make the component visible;
   *          <code>false</code> to make it invisible
   */
  void setVisible(boolean b);

}
