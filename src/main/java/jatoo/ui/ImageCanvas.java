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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import jatoo.image.ImageUtils;

/**
 * A "canvas" component where images can be painted, one at a time.
 * 
 * Some rules are applied to the painted image:
 * <ul>
 * <li>the image will be resized to fit the max size of the canvas</li>
 * <li>the ratio will be preserved</li>
 * <li>the resized image will be centered</li>
 * </ul>
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 2.0, June 26, 2017
 */
@SuppressWarnings("serial")
public class ImageCanvas extends JComponent {

  /** The image that the canvas paints. */
  private BufferedImage image;

  /** Interpolation hint value (how an image is scaled during a rendering operation). */
  private Object interpolationHint = RenderingHints.VALUE_INTERPOLATION_BILINEAR;

  /**
   * Creates a canvas instance with no image.
   */
  public ImageCanvas() {}

  /**
   * Creates a canvas instance with the specified image.
   * 
   * @param image
   *          the {@link BufferedImage} to be painted
   */
  public ImageCanvas(final BufferedImage image) {
    this.image = image;
  }

  /**
   * Updates the image this canvas will paint. If the value is null, nothing will be painted.
   * 
   * @param image
   *          the {@link BufferedImage} to be painted
   */
  public final void setImage(final BufferedImage image) {
    this.image = image;
    repaint();
  }

  /**
   * Returns the image this canvas paints.
   * 
   * @return the {@link BufferedImage} this canvas is painting, or <code>null</code> if there is no image to be painted
   */
  public final BufferedImage getImage() {
    return image;
  }

  public void setInterpolationNearestNeighbor() {
    interpolationHint = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
    repaint();
  }

  public void setInterpolationBilinear() {
    interpolationHint = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
    repaint();
  }

  public void setInterpolationBicubic() {
    interpolationHint = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
    repaint();
  }

  @Override
  protected final void paintComponent(final Graphics graphics) {
    super.paintComponent(graphics);

    //
    // no image, no paint

    if (image == null) {
      return;
    }

    //
    // create a new Graphics object to not alter the original

    final Graphics2D g = (Graphics2D) graphics.create();

    try {

      //
      // here we go

      int canvasWidth = getWidth();
      int canvasHeight = getHeight();

      paintImage(g, image, canvasWidth, canvasHeight);
    }

    //
    // make sure the created Graphics object is disposed

    finally {
      g.dispose();
    }
  }

  protected void paintImage(final Graphics2D g, final BufferedImage image, final int canvasWidth, final int canvasHeight) {

    Dimension imageDrawingSize = ImageUtils.calculateSizeToFit(image, canvasWidth, canvasHeight);

    int imageDrawingX = (canvasWidth - imageDrawingSize.width) / 2;
    int imageDrawingY = (canvasHeight - imageDrawingSize.height) / 2;

    paintImage(g, image, imageDrawingX, imageDrawingY, imageDrawingSize.width, imageDrawingSize.height);
  }

  protected void paintImage(final Graphics2D g, final BufferedImage image, final int x, final int y, final int width, final int height) {
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolationHint);
    g.drawImage(image, x, y, width, height, null);
  }

}
