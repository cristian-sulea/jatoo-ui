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

import jatoo.image.ImageUtils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
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
public class ImageViewerOldV1 extends JComponent {

  /** The logger. */
  private final Log logger = LogFactory.getLog(getClass());

  /** Zoom value representing the best fit. */
  private static final int ZOOM_BEST_FIT = 0;

  /** Zoom value representing the real size. */
  private static final int ZOOM_REAL_SIZE = 100;

  /** The zoom step value for wheel zoom in/out. */
  private static final int ZOOM_STEP = 20;

  /** The canvas used by this image viewer to display the images. */
  private final ImageCanvas imageCanvas;

  /**
   * We don't want the canvas to infinitely grow (the maximum size is based on
   * original image size with the zoom ratio applied).
   */
  private final Dimension imageCanvasMaxSize = new Dimension();

  /** The main container for image canvas. */
  private final JPanel imageCanvasContainer;

  /** The scroll pane container for image canvas. */
  private final JScrollPane imageCanvasScrollPane;

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

  /**
   * Creates a viewer instance with no image.
   */
  public ImageViewerOldV1() {
    this(null);
  }

  /**
   * Creates a viewer instance with the specified image.
   * 
   * @param image
   *          the {@link BufferedImage} to be displayed
   */
  public ImageViewerOldV1(final BufferedImage image) {

    imageCanvas = new ImageCanvas(image);

    imageCanvasContainer = new JPanel();
    imageCanvasContainer.setCursor(UITheme.getCursorDefault());
    imageCanvasContainer.add(imageCanvas);
    imageCanvasContainer.setLayout(new LayoutManager() {

      @Override
      public void layoutContainer(final Container container) {

        synchronized (container.getTreeLock()) {
          synchronized (imageCanvasMaxSize) {

            final int containerWidth = container.getWidth();
            final int containerHeight = container.getHeight();

            int imageCanvasX = 0;
            int imageCanvasY = 0;
            int imageCanvasWidth = containerWidth;
            int imageCanvasHeight = containerHeight;

            if (imageCanvasMaxSize.width == 0 || imageCanvasMaxSize.height == 0) {

              final BufferedImage image = imageCanvas.getImage();

              if (image != null) {

                final int imageWidth = image.getWidth();
                final int imageHeight = image.getHeight();

                if (containerWidth > imageWidth) {
                  imageCanvasWidth = imageWidth;
                  imageCanvasX = (containerWidth - imageCanvasWidth) / 2;
                }

                if (containerHeight > imageHeight) {
                  imageCanvasHeight = imageHeight;
                  imageCanvasY = (containerHeight - imageCanvasHeight) / 2;
                }
              }
            }

            else {

              if (containerWidth > imageCanvasMaxSize.width) {
                imageCanvasWidth = imageCanvasMaxSize.width;
                imageCanvasX = (containerWidth - imageCanvasWidth) / 2;
              }

              if (containerHeight > imageCanvasMaxSize.height) {
                imageCanvasHeight = imageCanvasMaxSize.height;
                imageCanvasY = (containerHeight - imageCanvasHeight) / 2;
              }
            }

            imageCanvas.setBounds(imageCanvasX, imageCanvasY, imageCanvasWidth, imageCanvasHeight);
          }
        }
      }

      @Override
      public Dimension preferredLayoutSize(final Container container) {

        synchronized (container.getTreeLock()) {
          synchronized (imageCanvasMaxSize) {

            //
            // is mandatory to be a NEW object

            return new Dimension(imageCanvasMaxSize);
          }
        }
      }

      @Override
      public Dimension minimumLayoutSize(final Container container) {

        synchronized (container.getTreeLock()) {
          synchronized (imageCanvasMaxSize) {

            //
            // is mandatory to be a NEW object

            return new Dimension(imageCanvasMaxSize);
          }
        }
      }

      public void addLayoutComponent(final String name, final Component comp) {}

      public void removeLayoutComponent(final Component comp) {}
    });

    imageCanvasScrollPane = new JScrollPane(imageCanvasContainer);
    imageCanvasScrollPane.setBorder(BorderFactory.createEmptyBorder());
    imageCanvasScrollPane.setViewportBorder(BorderFactory.createEmptyBorder());

    imageCanvasScrollPane.getViewport().addMouseWheelListener(imageCanvasScrollPaneWheelZoomListener);

    imageCanvasScrollPane.getViewport().addMouseMotionListener(imageCanvasScrollPaneDragToScrollListener);
    imageCanvasScrollPane.getViewport().addMouseListener(imageCanvasScrollPaneDragToScrollListener);

    setLayout(new GridLayout());
    add(imageCanvasScrollPane);

    //
    // just like JComponent and ImageCanvas
    // default value for opacity is transparent

    setOpaque(false);

    //
    // on constructor there is no need to call #updateImage()
    // if the image is null

    if (image != null) {
      updateImage();
    }
  }

  /**
   * Updates the image this viewer will display. If the value is null, nothing
   * will be showed.
   * 
   * @param image
   *          the {@link BufferedImage} to be displayed
   */
  public final void setImage(final BufferedImage image) {
    imageCanvas.setImage(image);
    updateImage();
  }

  /**
   * Returns the image this viewer shows.
   * 
   * @return the {@link BufferedImage} this viewer is showing, or
   *         <code>null</code> if there is no image to be showed
   */
  public final BufferedImage getImage() {
    return imageCanvas.getImage();
  }

  @Override
  public void setOpaque(boolean isOpaque) {
    super.setOpaque(isOpaque);

    imageCanvasContainer.setOpaque(isOpaque);
    imageCanvasScrollPane.setOpaque(isOpaque);
    imageCanvasScrollPane.getViewport().setOpaque(isOpaque);
  }

  @Override
  public synchronized void addMouseListener(MouseListener l) {
    imageCanvas.addMouseListener(l);
  }

  @Override
  public synchronized MouseListener[] getMouseListeners() {
    return imageCanvas.getMouseListeners();
  }

  @Override
  public synchronized void addMouseMotionListener(MouseMotionListener l) {
    imageCanvas.addMouseMotionListener(l);
  }

  @Override
  public synchronized MouseMotionListener[] getMouseMotionListeners() {
    return imageCanvas.getMouseMotionListeners();
  }

  @Override
  public synchronized void addMouseWheelListener(MouseWheelListener l) {
    imageCanvas.addMouseWheelListener(l);
  }

  @Override
  public synchronized MouseWheelListener[] getMouseWheelListeners() {
    return imageCanvas.getMouseWheelListeners();
  }

  /**
   * Zoom the image to the specified (percentage) value.
   * 
   * @param zoom
   *          the new zoom, as a percent
   */
  public final void setZoom(final int zoom) {

    if (logger.isDebugEnabled()) {
      logger.debug("new zoom: " + zoom + " (old zoom: " + this.zoom + ")");
    }

    //
    // size and visible rectangle before zooming

    final Dimension oldImageCanvasContainerSize = imageCanvasContainer.getSize();
    final Rectangle oldImageCanvasContainerVisibleRect = imageCanvasContainer.getVisibleRect();

    //
    // calculate the center of the visible rectangle

    final int oldImageCanvasContainerVisibleRectCenterX = oldImageCanvasContainerVisibleRect.x + oldImageCanvasContainerVisibleRect.width / 2;
    final int oldImageCanvasContainerVisibleRectCenterY = oldImageCanvasContainerVisibleRect.y + oldImageCanvasContainerVisibleRect.height / 2;

    //
    // zoom ( set the new zoom value and update image )

    this.zoom = zoom;
    updateImage();

    //
    // zoomed size

    final Dimension zoomedImageCanvasContainerSize = imageCanvasContainer.getSize();

    //
    // now we can calculate the ratio (new size vs old size)

    final double xRatio = zoomedImageCanvasContainerSize.getWidth() / oldImageCanvasContainerSize.getWidth();
    final double yRatio = zoomedImageCanvasContainerSize.getHeight() / oldImageCanvasContainerSize.getHeight();

    //
    // apply the ratio to the old center

    int newImageCanvasContainerVisibleRectCenterX = (int) (oldImageCanvasContainerVisibleRectCenterX * xRatio);
    int newImageCanvasContainerVisibleRectCenterY = (int) (oldImageCanvasContainerVisibleRectCenterY * yRatio);

    //
    // calculate the new visible rectangle (using the translated center)

    Rectangle newImageCanvasContainerVisibleRect = new Rectangle(oldImageCanvasContainerVisibleRect.getSize());
    newImageCanvasContainerVisibleRect.x = newImageCanvasContainerVisibleRectCenterX - oldImageCanvasContainerVisibleRect.width / 2;
    newImageCanvasContainerVisibleRect.y = newImageCanvasContainerVisibleRectCenterY - oldImageCanvasContainerVisibleRect.height / 2;

    //
    // scroll to make visible the new rectangle

    imageCanvasContainer.scrollRectToVisible(newImageCanvasContainerVisibleRect);
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
   * Zooms out with the specified (percentage) value. For example if the current
   * zoom is 80%, a zoom out with 20 as step will result in a 60% final zoom.
   * 
   * @param zoomStep
   *          zoom out step
   */
  public final void zoomOut(final int zoomStep) {

    final BufferedImage image = imageCanvas.getImage();

    //
    // no image, no zoom

    if (image == null) {
      return;
    }

    int zoomTmp = this.zoom;

    //
    // if zoom is best fit
    // calculate the zoom based on container size

    if (zoomTmp == ZOOM_BEST_FIT) {
      zoomTmp = ImageUtils.calculateSizeToFit(image, imageCanvasContainer.getSize()).width * ZOOM_REAL_SIZE / image.getWidth();
    }

    //
    // calculate new zoom based on the step parameter

    int newZoom = (int) Math.floor(((double) zoomTmp) / ((double) zoomStep)) * zoomStep;

    if (newZoom == zoomTmp) {
      newZoom = newZoom - zoomStep;
    }

    if (newZoom > 0) {
      setZoom(newZoom);
    }

    //
    // ignore if new zoom <= 0

    else {
      if (logger.isDebugEnabled()) {
        logger.debug("new zoom <= 0 (zoom out is ignored)");
      }
    }
  }

  /**
   * Zooms in with the specified (percentage) value. For example if the current
   * zoom is 60%, a zoom in with 20 as step will result in a 80% final zoom.
   * 
   * @param zoomStep
   *          zoom out step
   */
  public final void zoomIn(final int zoomStep) {

    final BufferedImage image = imageCanvas.getImage();

    //
    // no image, no zoom

    if (image == null) {
      return;
    }

    int zoomTmp = this.zoom;

    //
    // if zoom is best fit
    // calculate the zoom based on container size

    if (zoomTmp == ZOOM_BEST_FIT) {
      zoomTmp = ImageUtils.calculateSizeToFit(image, imageCanvasContainer.getSize()).width * ZOOM_REAL_SIZE / image.getWidth();
    }

    //
    // calculate new zoom based on the step parameter

    int newZoom = (int) (Math.floor(((double) zoomTmp) / ((double) zoomStep)) + 1) * zoomStep;

    if (newZoom == zoomTmp) {
      newZoom = newZoom + zoomStep;
    }

    setZoom(newZoom);
  }

  /**
   * Enables the "best fit" mode (the image will be always 100%).
   */
  public final void setBestFit() {
    setZoom(ZOOM_BEST_FIT);
  }

  /**
   * Determines whether this viewer is on "best fit" mode (the image will be
   * always 100%).
   * 
   * @return <code>true</code> if the viewer is on "best fit" mode,
   *         <code>false</code> otherwise
   */
  public final boolean isBestFit() {
    return zoom == ZOOM_BEST_FIT;
  }

  /**
   * Zooms the image to 100%.
   */
  public final void setRealSize() {
    setZoom(ZOOM_REAL_SIZE);
  }

  /**
   * Determines whether this viewer shows the image at the "real size" (100%).
   * 
   * @return <code>true</code> if the viewer shows the image at "real size"
   *         (100%), <code>false</code> otherwise
   */
  public final boolean isRealSize() {
    return zoom == ZOOM_REAL_SIZE;
  }

  /**
   * Updates the components of the image viewer. This is the place where:
   * <ul>
   * <li>image canvas maximum size is calculated</li>
   * <li>container and scroll pane are revalidated</li>
   * <li>cursor is updated</li>
   * </ul>
   */
  private void updateImage() {

    final BufferedImage image = imageCanvas.getImage();

    //
    // no image, no size

    if (image == null) {
      synchronized (imageCanvasMaxSize) {
        imageCanvasMaxSize.width = 0;
        imageCanvasMaxSize.height = 0;
      }
    }

    else {

      final int imageWidth = image.getWidth();
      final int imageHeight = image.getHeight();

      //
      // if zoom is set to real size there is no need for calculations

      if (zoom == ZOOM_REAL_SIZE) {
        synchronized (imageCanvasMaxSize) {
          imageCanvasMaxSize.width = imageWidth;
          imageCanvasMaxSize.height = imageHeight;
        }
      }

      //
      // apply the zoom ratio to the image size

      else {

        final double ratio = ((double) zoom) / ((double) ZOOM_REAL_SIZE);

        final double imageCanvasMaxWidth = ((double) imageWidth) * ratio;
        final double imageCanvasMaxHeight = ((double) imageHeight) * ratio;

        synchronized (imageCanvasMaxSize) {
          imageCanvasMaxSize.width = (int) imageCanvasMaxWidth;
          imageCanvasMaxSize.height = (int) imageCanvasMaxHeight;
        }
      }
    }

    //
    // revalidation
    // do not use #revaliade() method, validation will occur after all currently
    // pending events have been dispatched, use invalidate/validate/repaint

    imageCanvasContainer.invalidate();
    imageCanvasContainer.validate();
    imageCanvasContainer.repaint();

    imageCanvasScrollPane.invalidate();
    imageCanvasScrollPane.validate();
    imageCanvasScrollPane.repaint();

    //
    // this is the best place to update the cursor
    // since all actions on the image will call this method

    updateCursor(false);
  }

  /**
   * Mouse listener for zooming the image in and out with the mouse wheel.
   */
  private class WheelZoomListener implements MouseWheelListener {

    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {

      if (e.getPreciseWheelRotation() > 0) {
        zoomOut(ZOOM_STEP);
      } else {
        zoomIn(ZOOM_STEP);
      }
    }
  }

  /**
   * Mouse listener for dragging the image with the mouse.
   */
  private class DragToScrollListener extends MouseInputAdapter {

    /**
     * The X coordinate for mouse pressed event, used to calculate the
     * horizontal drag distance.
     */
    private int x;

    /**
     * The Y coordinate for mouse pressed event, used to calculate the vertical
     * drag distance.
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

      final Point pointZero = SwingUtilities.convertPoint(imageCanvasScrollPane.getViewport(), 0, 0, imageCanvasContainer);
      final Rectangle viewRectangle = imageCanvasScrollPane.getViewport().getViewRect();

      imageCanvasContainer.scrollRectToVisible(new Rectangle(pointZero.x - xDragged, pointZero.y - yDragged, viewRectangle.width, viewRectangle.height));

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

    final Dimension imageCanvasContainerSize = imageCanvasContainer.getSize();
    final Dimension imageCanvasScrollPaneViewportVisibleSize = imageCanvasScrollPane.getViewport().getExtentSize();

    final boolean isDifferentWidth = imageCanvasContainerSize.width > imageCanvasScrollPaneViewportVisibleSize.width;
    final boolean isDifferentHeight = imageCanvasContainerSize.height > imageCanvasScrollPaneViewportVisibleSize.height;

    //
    // the old condition was:
    // imageCanvasScrollPane.getVerticalScrollBar().isVisible() ||
    // imageCanvasScrollPane.getHorizontalScrollBar().isVisible()

    if (isDifferentWidth || isDifferentHeight) {

      if (isDragging) {
        imageCanvasContainer.setCursor(UITheme.getCursorDragging());
      } else {
        imageCanvasContainer.setCursor(UITheme.getCursorDrag());
      }
    }

    else {
      imageCanvasContainer.setCursor(UITheme.getCursorDefault());
    }
  }
}
