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
