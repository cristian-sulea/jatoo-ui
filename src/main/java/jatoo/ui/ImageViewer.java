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

import java.awt.Container;
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
 * @version 5.5, March 14, 2019
 */
@SuppressWarnings("serial")
public class ImageViewer extends JScrollPane {

  /** The logger. */
  private final Log logger = LogFactory.getLog(getClass());

  /** Zoom value representing the best fit. */
  private static final int ZOOM_BEST_FIT = 0;

  /** Zoom value representing the real size. */
  private static final int ZOOM_REAL_SIZE = 100;

  /** Zoom minimum size. */
  private static final int ZOOM_MIN_VALUE = ZOOM_BEST_FIT;

  /** Zoom maximum size. */
  private static final int ZOOM_MAX_VALUE = ZOOM_REAL_SIZE * 4;

  /** The canvas used by this image viewer to display the images. */
  private final ImageCanvas canvas = new ImageCanvas();

  /** Image zoom with mouse wheel. */
  private final WheelZoomListener wheelZoomListener = new WheelZoomListener();

  /** Image drag to scroll. */
  private final DragToScrollListener dragToScrollListener = new DragToScrollListener();

  /** The image this viewer will display. */
  private BufferedImage image;

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
  public ImageViewer() {

    setViewportView(canvas);

    setBorder(BorderFactory.createEmptyBorder());
    setViewportBorder(BorderFactory.createEmptyBorder());

    getViewport().addMouseWheelListener(wheelZoomListener);

    getViewport().addMouseMotionListener(dragToScrollListener);
    getViewport().addMouseListener(dragToScrollListener);

    //
    // don't forget about the cursor

    updateCursor(false);
  }

  /**
   * Creates a viewer instance with the specified image.
   * 
   * @param image
   *          the {@link BufferedImage} to be displayed
   */
  public ImageViewer(final BufferedImage image) {
    this();
    setImage(image);
  }

  /**
   * Gets the canvas used to paint the image.
   * 
   * @return the {@link ImageCanvas} object
   */
  public final ImageCanvas getCanvas() {
    return canvas;
  }

  /**
   * Updates the image this viewer will display. If the value is <code>null</code>, nothing will be showed.
   * 
   * @param image
   *          the {@link BufferedImage} to be displayed
   */
  public final void setImage(final BufferedImage image) {
    this.image = image;

    boolean isImageSmaller = isImageSmaller();

    // if (isImageSmaller) {
    // canvas.setInterpolationNearestNeighbor();
    // } else {
    // canvas.setInterpolationBicubic();
    // }

    canvas.setImage(image);

    if (isImageSmaller) {
      zoom(ZOOM_REAL_SIZE);
    } else {
      zoom(ZOOM_BEST_FIT);
    }
  }

  /**
   * Returns the image this viewer shows.
   * 
   * @return the {@link BufferedImage} this viewer is showing, or <code>null</code> if there is no image to be showed
   */
  public final BufferedImage getImage() {
    return image;
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
   * @param newZoom
   *          the new zoom, as a percent
   */
  public final void zoom(final int newZoom) {

    if (image != null) {

      if (logger.isDebugEnabled()) {
        logger.debug("new zoom: " + newZoom + " (old zoom: " + zoom + ")");
      }

      if (newZoom < ZOOM_MIN_VALUE || newZoom > ZOOM_MAX_VALUE) {

        if (logger.isDebugEnabled()) {
          logger.debug("new zoom (" + newZoom + ") is out of range [" + ZOOM_MIN_VALUE + ", " + ZOOM_MAX_VALUE + "]");
        }

        return;
      }

      zoom = newZoom;

      //
      // remember canvas size and visible rectangle before zooming

      final Dimension oldCanvasSize = canvas.getSize();
      final Rectangle oldCanvasVisibleRect = canvas.getVisibleRect();

      //
      // reset canvas size if zoom is set to best fit

      if (zoom == ZOOM_BEST_FIT) {
        canvas.setPreferredSize(null);
        canvas.setPaintRealSize(false);
      }

      else {

        // System.out.println(getViewport().getWidth());
        // canvas.setFillCanvasWithSmallerImage(getViewport().getWidth() < image.getWidth());

        final int imageWidth = image.getWidth();
        final int imageHeight = image.getHeight();

        //
        // there is no need for calculations if zoom is set to real size

        if (zoom == ZOOM_REAL_SIZE) {

          if (isImageSmaller()) {
            canvas.setPreferredSize(null);
            canvas.setPaintRealSize(true);
          }

          else {
            canvas.setPreferredSize(new Dimension(imageWidth, imageHeight));
            canvas.setPaintRealSize(false);
          }
        }

        //
        // apply the zoom ratio to the image size

        else {

          final double ratio = ((double) zoom) / ((double) ZOOM_REAL_SIZE);

          final double canvasWidth = ((double) (imageWidth)) * ratio;
          final double canvasHeight = ((double) (imageHeight)) * ratio;

          canvas.setPreferredSize(new Dimension((int) canvasWidth, (int) canvasHeight));
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

      // Point mousePoint = getMousePosition();
      // Dimension size = getSize();
      // int xSlide = mousePoint.x < size.width / 2 ? -1 : +1;
      // int YSlide = mousePoint.y < size.height / 2 ? -1 : +1;
      //
      // zoomedCanvasVisibleRect.x += (xSlide * (size.width*5/100));
      // zoomedCanvasVisibleRect.y += (YSlide * (size.height*5/100));

      canvas.scrollRectToVisible(zoomedCanvasVisibleRect);
    }

    //
    // no need to do anything if the image is null

    else {
      canvas.setPreferredSize(null);
    }

    //
    // don't forget about the cursor

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

  public final void zoomOut() {

    if (isImageSmaller()) {
      return;
    }

    zoomOut(getAutoZoomStep());

    if (new Rectangle(getViewport().getSize()).contains(new Rectangle(canvas.getSize()))) {
      if (getZoom() > ZOOM_MIN_VALUE && getZoom() < ZOOM_MAX_VALUE) {
        zoomOut();
      }
    }
  }

  public final void zoomIn() {

    if (isImageSmaller()) {
      return;
    }

    zoomIn(getAutoZoomStep());

    if (new Rectangle(getViewport().getSize()).contains(new Rectangle(canvas.getSize()))) {
      if (getZoom() > ZOOM_MIN_VALUE && getZoom() < ZOOM_MAX_VALUE) {
        zoomIn();
      }
    }
  }

  private int getAutoZoomStep() {
    if (zoom >= ZOOM_REAL_SIZE * 2) {
      return zoomStep * 5;
    } else if (zoom >= ZOOM_REAL_SIZE) {
      return zoomStep * 2;
    } else {
      return zoomStep;
    }
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
   * The scroll bars are displayed only when needed.
   */
  public void setScrollBarsVisibilityToAsNeeded() {
    setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  }

  /**
   * The scroll bars are never displayed.
   */
  public void setScrollBarsVisibilityToNever() {
    setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
  }

  /**
   * The scroll bars are always displayed.
   */
  public void setScrollBarsVisibilityToAlways() {
    setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
  }

  private boolean isImageSmaller() {

    if (image == null) {
      return true;
    }

    return image.getWidth() < getWidth() && image.getHeight() < getHeight();
  }

  /**
   * Mouse listener for zooming the image in and out with the mouse wheel.
   */
  private class WheelZoomListener implements MouseWheelListener {

    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {

      if (isImageSmaller()) {
        return;
      }

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
   * Updates the mouse cursor based on various factors.
   * 
   * @param isDragging
   *          indicates that there is a drag action
   */
  private void updateCursor(final boolean isDragging) {

    if (isImageSmaller()) {
      canvas.setCursor(UITheme.getCursorDefault());
    }

    else {

      if (isBestFit()) {
        canvas.setCursor(UITheme.getCursorDefault());
      }

      else {

        if (isDragging) {
          canvas.setCursor(UITheme.getCursorDragging());
        } else {
          canvas.setCursor(UITheme.getCursorDrag());
        }
      }
    }

    //
    // hack for repaint overlapping transparent components

    Container c = getTopLevelAncestor();
    if (c != null) {
      c.repaint();
    }
  }

}
