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
import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * Improved {@link JPanel} with many new things like opacity.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.0, July 10, 2014
 */
@SuppressWarnings("serial")
public class Panel extends JPanel implements AlphaComponent {

  /** The helper for opacity. */
  private final AlphaComponentHelper alphaHelper = new AlphaComponentHelper(this);

  public Panel() {
    super();
  }

  public Panel(LayoutManager layout) {
    super(layout);
  }

  public Panel(boolean isDoubleBuffered) {
    super(isDoubleBuffered);
  }

  public Panel(LayoutManager layout, boolean isDoubleBuffered) {
    super(layout, isDoubleBuffered);
  }

  //
  //

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

    synchronized (getTreeLock()) {

      for (Component component : getComponents()) {

        if (component instanceof AlphaComponent) {
          ((AlphaComponent) component).setAlpha(alpha);
        }
      }
    }
  }

  @Override
  public void setAlpha(float alpha, boolean visibility) {
    alphaHelper.setAlpha(alpha, visibility);

    synchronized (getTreeLock()) {

      for (Component component : getComponents()) {

        if (component instanceof AlphaComponent) {
          ((AlphaComponent) component).setAlpha(alpha, visibility);
        }
      }
    }
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
