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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A "viewer" component where images can be displayed, one at a time.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 3.5, July 31, 2014
 */
@SuppressWarnings("serial")
public class ImageViewerV3 extends JScrollPane {

  /** The logger. */
  private final Log logger = LogFactory.getLog(getClass());

  /** Zoom value representing the best fit. */
  private static final int ZOOM_BEST_FIT = 0;

  /** Zoom value representing the real size. */
  private static final int ZOOM_REAL_SIZE = 100;

  /** The canvas used by this image viewer to display the images. */
  private final ImageCanvas canvas;

  /** Image zoom with mouse wheel. */
  private final WheelZoomListener imageCanvasScrollPaneWheelZoomListener = new WheelZoomListener();

  /** Image drag to scroll. */
  private final DragToScrollListener imageCanvasScrollPaneDragToScrollListener = new DragToScrollListener();

  /**
   * The zoom percentage for the image to be displayed.
   * <p>
   * Possible values:
   * <ul>
   * <li>{@value #ZOOM_BEST_FIT} - best fit (see: {@link #ZOOM_BEST_FIT})</li>
   * <li>{@value #ZOOM_BEST_FIT} - real size (see: {@link #ZOOM_BEST_FIT}</li>
   * <li>[1..) - zoom percentage</li>
   * </ul>
   */
  private int zoom = ZOOM_BEST_FIT;

  /** The zoom step value zoom in/out. */
  private int zoomStep = 10;

  /**
   * Creates a viewer instance with no image.
   */
  public ImageViewerV3() {

    canvas = new ImageCanvas();
    canvas.setCursor(UITheme.getCursorDefault());

    setViewportView(canvas);
    setBorder(BorderFactory.createEmptyBorder());
    setViewportBorder(BorderFactory.createEmptyBorder());

    getViewport().addMouseWheelListener(imageCanvasScrollPaneWheelZoomListener);

    getViewport().addMouseMotionListener(imageCanvasScrollPaneDragToScrollListener);
    getViewport().addMouseListener(imageCanvasScrollPaneDragToScrollListener);
  }

  /**
   * Creates a viewer instance with the specified image.
   * 
   * @param image
   *          the {@link BufferedImage} to be displayed
   */
  public ImageViewerV3(final BufferedImage image) {
    this();
    setImage(image);
  }

  /**
   * Updates the image this viewer will display. If the value is null, nothing will be showed.
   * 
   * @param image
   *          the {@link BufferedImage} to be displayed
   */
  public final void setImage(final BufferedImage image) {
    canvas.setImage(image);
    zoom(ZOOM_BEST_FIT);
  }

  /**
   * Returns the image this viewer shows.
   * 
   * @return the {@link BufferedImage} this viewer is showing, or <code>null</code> if there is no image to be showed
   */
  public final BufferedImage getImage() {
    return canvas.getImage();
  }

  public void setZoomStep(final int zoomStep) {
    this.zoomStep = zoomStep;
  }

  public int getZoomStep() {
    return zoomStep;
  }

  /**
   * Zoom the image to the specified (percentage) value.
   * 
   * @param zoom
   *          the new zoom, as a percent
   */
  public final void zoom(final int zoom) {

    if (zoom < ZOOM_BEST_FIT || zoom > ZOOM_REAL_SIZE * 3) {
      return;
    }

    if (logger.isDebugEnabled()) {
      logger.debug("new zoom: " + zoom + " (old zoom: " + this.zoom + ")");
    }

    this.zoom = zoom;

    final BufferedImage image = canvas.getImage();

    //
    // remember canvas size and visible rectangle before zooming

    final Dimension oldCanvasSize = canvas.getSize();
    final Rectangle oldCanvasVisibleRect = canvas.getVisibleRect();

    //
    // no image, no size

    if (image == null || zoom == ZOOM_BEST_FIT) {
      canvas.setPreferredSize(null);
    }

    else {

      final int imageWidth = image.getWidth();
      final int imageHeight = image.getHeight();

      //
      // if zoom is set to real size there is no need for calculations

      if (zoom == ZOOM_REAL_SIZE) {
        canvas.setPreferredSize(new Dimension(imageWidth, imageHeight));
      }

      //
      // apply the zoom ratio to the image size

      else {

        final double ratio = ((double) zoom) / ((double) ZOOM_REAL_SIZE);

        final double canvasWidth = ((double) (imageWidth - getViewport().getWidth())) * ratio;
        final double canvasHeight = ((double) (imageHeight - getViewport().getHeight())) * ratio;

        canvas.setPreferredSize(new Dimension((int) canvasWidth + getViewport().getWidth(), (int) canvasHeight + getViewport().getHeight()));
      }
    }

    //
    // revalidation
    // do not use #revaliade() method, validation will occur after all currently
    // pending events have been dispatched, use invalidate/validate/repaint

    canvas.invalidate();
    canvas.validate();
    canvas.repaint();

    invalidate();
    validate();
    repaint();

    //
    // update the visible rectangle (after the zooming):
    //
    // - calculate the center of the old visible rectangle
    // - calculate the ratio (new size vs old size)
    // - apply the ratio to the old center
    // - calculate the new visible rectangle (using the translated center)
    // - scroll to make visible the new rectangle

    final int oldCanvasVisibleRectCenterX = oldCanvasVisibleRect.x + oldCanvasVisibleRect.width / 2;
    final int oldCanvasVisibleRectCenterY = oldCanvasVisibleRect.y + oldCanvasVisibleRect.height / 2;

    final Dimension zoomedCanvasSize = canvas.getSize();

    final double xRatio = zoomedCanvasSize.getWidth() / oldCanvasSize.getWidth();
    final double yRatio = zoomedCanvasSize.getHeight() / oldCanvasSize.getHeight();

    final int zoomedCanvasVisibleRectCenterX = (int) (oldCanvasVisibleRectCenterX * xRatio);
    final int zoomedCanvasVisibleRectCenterY = (int) (oldCanvasVisibleRectCenterY * yRatio);

    Rectangle zoomedCanvasVisibleRect = new Rectangle(oldCanvasVisibleRect.getSize());
    zoomedCanvasVisibleRect.x = zoomedCanvasVisibleRectCenterX - oldCanvasVisibleRect.width / 2;
    zoomedCanvasVisibleRect.y = zoomedCanvasVisibleRectCenterY - oldCanvasVisibleRect.height / 2;

    canvas.scrollRectToVisible(zoomedCanvasVisibleRect);

    //
    // TODO: Do i really need to update the cursor at this point?

    updateCursor(false);
  }

  /**
   * Returns the actual zoom of the viewer.
   * 
   * @return the actual zoom, as a percent
   */
  public final int getZoom() {
    return zoom;
  }

  /**
   * Zooms out with the specified (percentage) value. For example if the current zoom is 80%, a zoom out with 20 as step
   * will result in a 60% final zoom.
   * 
   * @param zoomStep
   *          zoom out step
   */
  public final void zoomOut(final int zoomStep) {
    zoom(zoom - zoomStep);
  }

  public final void zoomOut() {
    zoomOut(zoomStep);
  }

  /**
   * Zooms in with the specified (percentage) value. For example if the current zoom is 60%, a zoom in with 20 as step
   * will result in a 80% final zoom.
   * 
   * @param zoomStep
   *          zoom out step
   */
  public final void zoomIn(final int zoomStep) {
    zoom(zoom + zoomStep);
  }

  public final void zoomIn() {
    zoomIn(zoomStep);
  }

  /**
   * Enables the "best fit" mode (the image will be always 100%).
   */
  public final void setBestFit() {
    zoom(ZOOM_BEST_FIT);
  }

  /**
   * Determines whether this viewer is on "best fit" mode (the image will be always 100%).
   * 
   * @return <code>true</code> if the viewer is on "best fit" mode, <code>false</code> otherwise
   */
  public final boolean isBestFit() {
    return zoom == ZOOM_BEST_FIT;
  }

  /**
   * Zooms the image to 100%.
   */
  public final void setRealSize() {
    zoom(ZOOM_REAL_SIZE);
  }

  /**
   * Determines whether this viewer shows the image at the "real size" (100%).
   * 
   * @return <code>true</code> if the viewer shows the image at "real size" (100%), <code>false</code> otherwise
   */
  public final boolean isRealSize() {
    return zoom == ZOOM_REAL_SIZE;
  }

  /**
   * Mouse listener for zooming the image in and out with the mouse wheel.
   */
  private class WheelZoomListener implements MouseWheelListener {

    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {
      if (e.getPreciseWheelRotation() > 0) {
        zoomOut();
      } else {
        zoomIn();
      }
    }
  }

  /**
   * Mouse listener for dragging the image with the mouse.
   */
  private class DragToScrollListener extends MouseInputAdapter {

    /**
     * The X coordinate for mouse pressed event, used to calculate the horizontal drag distance.
     */
    private int x;

    /**
     * The Y coordinate for mouse pressed event, used to calculate the vertical drag distance.
     */
    private int y;

    @Override
    public void mousePressed(final MouseEvent e) {

      updateCursor(true);

      x = e.getX();
      y = e.getY();
    }

    @Override
    public void mouseDragged(final MouseEvent e) {

      final int xDragged = e.getX() - x;
      final int yDragged = e.getY() - y;

      final Point pointZero = SwingUtilities.convertPoint(getViewport(), 0, 0, canvas);
      final Rectangle viewRectangle = getViewport().getViewRect();

      canvas.scrollRectToVisible(new Rectangle(pointZero.x - xDragged, pointZero.y - yDragged, viewRectangle.width, viewRectangle.height));

      x = e.getX();
      y = e.getY();
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
      updateCursor(false);
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
      updateCursor(false);
    }
  }

  /**
   * Updates the mouse cursor based on various factors. For example:
   * <ul>
   * <li>if drag is possible - {@link UITheme#getCursorDrag()}</li>
   * <li>if is a drag - {@link UITheme#getCursorDragging()}</li>
   * <li>when is size to fit - {@link UITheme#getCursorDefault()}</li>
   * </ul>
   * 
   * @param isDragging
   *          indicates that there is a drag action
   */
  private void updateCursor(final boolean isDragging) {

    final Dimension imageCanvasContainerSize = canvas.getSize();
    final Dimension imageCanvasScrollPaneViewportVisibleSize = getViewport().getExtentSize();

    final boolean isDifferentWidth = imageCanvasContainerSize.width > imageCanvasScrollPaneViewportVisibleSize.width;
    final boolean isDifferentHeight = imageCanvasContainerSize.height > imageCanvasScrollPaneViewportVisibleSize.height;

    //
    // the old condition was:
    // imageCanvasScrollPane.getVerticalScrollBar().isVisible() ||
    // imageCanvasScrollPane.getHorizontalScrollBar().isVisible()

    if (isDifferentWidth || isDifferentHeight) {

      if (isDragging) {
        canvas.setCursor(UITheme.getCursorDragging());
      } else {
        canvas.setCursor(UITheme.getCursorDrag());
      }
    }

    else {
      canvas.setCursor(UITheme.getCursorDefault());
    }
  }
}