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
 * A "canvas" component where images can be painted, one at a time. Some rules are applied to the painted image:
 * <ul>
 * <li>the image will be resized to fit the max size of the canvas</li>
 * <li>the ratio will be preserved</li>
 * <li>the resized image will be centered</li>
 * </ul>
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.3, June 14, 2017
 */
@SuppressWarnings("serial")
public class ImageCanvas extends JComponent {

  /** The image that the canvas paints. */
  private BufferedImage image;

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

  @Override
  protected final void paintComponent(final Graphics graphics) {
    super.paintComponent(graphics);

    //
    // create a new Graphics object to not alter the original

    final Graphics2D g = (Graphics2D) graphics.create();

    try {

      //
      // no image, no paint

      if (image == null) {
        return;
      }

      //
      // here we go

      final int canvasWidth = getWidth();
      final int canvasHeight = getHeight();

      //
      // if image size is same as canvas size
      // there is no need for resize operations

      final int imageWidth = image.getWidth();
      final int imageHeight = image.getHeight();

      if (imageWidth == canvasWidth && imageHeight == canvasHeight) {
        g.drawImage(image, 0, 0, null);
      }

      //
      // different sizes means
      // resize and center the painting image

      else {

        final Dimension paintingImageSize = ImageUtils.calculateSizeToFit(image, canvasWidth, canvasHeight);

        final int x = (canvasWidth - paintingImageSize.width) / 2;
        final int y = (canvasHeight - paintingImageSize.height) / 2;

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, x, y, paintingImageSize.width, paintingImageSize.height, null);
      }
    }

    //
    // make sure the created Graphics object is disposed

    finally {
      g.dispose();
    }
  }

}
