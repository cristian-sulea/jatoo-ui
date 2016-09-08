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

import jatoo.image.ImageUtils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Opacity helper for UI components (implementing interface
 * {@link AlphaComponent}).
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.0, July 10, 2014
 */
public class AlphaComponentHelper {

  /** The component. */
  private final AlphaComponent component;

  /** The opacity level of the component. */
  private float alpha = 1f;

  /** Sometimes we don;t want to paint the background. */
  private boolean paintBackground = true;

  /** The buffer image used to paint the component. */
  private BufferedImage imageBuffer = null;

  public AlphaComponentHelper(AlphaComponent component) {
    this.component = component;
  }

  public void paintComponent(Graphics g) {

    final int width = component.getWidth();
    final int height = component.getHeight();

    //
    // improve the performance: if (opacity == 1f) then call super.paint(g)

    if (alpha == 1f) {

      if (paintBackground) {

        final Color background = component.getBackground();

        if (background != null) {
          g.setColor(background);
          g.fillRect(0, 0, width, height);
        }
      }

      component.superPaintComponent(g);
    }

    //
    // do this only if (opacity != 1f)

    else {

      if (imageBuffer == null || imageBuffer.getWidth() != width || imageBuffer.getHeight() != height) {
        imageBuffer = ImageUtils.create(width, height, true);
      }

      Graphics2D imageGraphics = (Graphics2D) imageBuffer.getGraphics();

      imageGraphics.setClip(g.getClip());

      imageGraphics.setFont(g.getFont());
      imageGraphics.setColor(g.getColor());

      imageGraphics.setRenderingHints(((Graphics2D) g).getRenderingHints());

      Composite imageComposite = imageGraphics.getComposite();
      imageGraphics.setComposite(AlphaComposite.Clear);
      imageGraphics.fillRect(0, 0, width, height);
      imageGraphics.setComposite(imageComposite);

      component.superPaintComponent(imageGraphics);
      component.superPaintBorder(imageGraphics);

      Graphics2D g2d = (Graphics2D) g.create();
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

      if (paintBackground) {

        final Color background = component.getBackground();

        if (background != null) {
          g2d.setColor(background);
          g2d.fillRect(0, 0, width, height);
        }
      }

      g2d.drawImage(imageBuffer, 0, 0, null);
    }
  }

  public void paintBorder(Graphics g) {
    if (alpha == 1f) {
      component.superPaintBorder(g);
    }
  }

  public boolean isOpaque() {
    return false;
  }

  /**
   * Sets the opacity of the alpha component.
   * <p>
   * The opacity value is in the range [0.0f .. 1.0f].
   * 
   * @param alpha
   *          the opacity level
   */
  public final void setAlpha(final float alpha) {

    if (alpha < 0f || alpha > 1f) {
      throw new IllegalArgumentException("The opacity value (alpha = " + alpha + ") should be in the range [0.0f .. 1.0f].");
    }

    this.alpha = alpha;

    component.repaint();
  }

  /**
   * Sets the opacity of the alpha component and updates the visibility (using
   * method {@link #setVisible(boolean)}).
   * <p>
   * The opacity value is in the range [0.0f .. 1.0f].
   * 
   * @param alpha
   *          the opacity level
   * @param visibility
   *          <code>true</code> to make the button visible, <code>false</code>
   *          to make it invisible (using method {@link #setVisible(boolean)})
   */
  public final void setAlpha(final float alpha, final boolean visibility) {
    setAlpha(alpha);
    component.setVisible(visibility);
  }

  /**
   * Enable or disable the paint of the background.
   * 
   * @param paintBackground
   *          <code>true</code> to faint the background, <code>false</code>
   *          otherwise
   */
  public void setPaintBackground(boolean paintBackground) {
    this.paintBackground = paintBackground;
  }

}
