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

import java.awt.Graphics;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Improved {@link JButton} with many new things like opacity.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.1, July 10, 2014
 */
@SuppressWarnings("serial")
public class Button extends JButton implements AlphaComponent {

  /** The helper for opacity. */
  private final AlphaComponentHelper alphaHelper = new AlphaComponentHelper(this);

  /**
   * Creates a button with no text or icon.
   */
  public Button() {
    super();
  }

  /**
   * Creates a button with text.
   * 
   * @param text
   *          the text of the button
   */
  public Button(String text) {
    super(text);
  }

  /**
   * Creates a button with an icon.
   * 
   * @param icon
   *          the Icon image to display on the button
   */
  public Button(Icon icon) {
    super(icon);
  }

  /**
   * Creates a button with initial text and an icon.
   * 
   * @param text
   *          the text of the button
   * @param icon
   *          the Icon image to display on the button
   */
  public Button(String text, Icon icon) {
    super(text, icon);
  }

  /**
   * Creates a button where properties are taken from the {@link Action}
   * supplied.
   * 
   * @param action
   *          the {@link Action} used to specify the new button
   */
  public Button(Action action) {
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
