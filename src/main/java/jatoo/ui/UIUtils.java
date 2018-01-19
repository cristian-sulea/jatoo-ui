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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.JTextComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A collection of utility methods to ease the work with UI components.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 4.1, January 19, 2018
 */
public class UIUtils {

  /** the logger */
  private static final Log logger = LogFactory.getLog(UIUtils.class);

  //
  // --- Forward DRAG events as MOVE
  //

  private static final Insets NO_MARGINS_GLUE_GAPS = new Insets(0, 0, 0, 0);

  public static void forwardDragAsMove(Component source, Component target) {
    forwardDragAsMove(source, target, 0, null);
  }

  public static void forwardDragAsMove(final Component source, final Component target, final int marginsGlueRange) {
    forwardDragAsMove(source, target, marginsGlueRange, NO_MARGINS_GLUE_GAPS);
  }

  public static void forwardDragAsMove(final Component source, final Component target, final int marginsGlueRange, final Insets marginsGlueGaps) {

    final Point startPoint = new Point();

    //
    // get the start point

    source.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {

        if (!SwingUtilities.isLeftMouseButton(e)) {
          startPoint.setLocation(Integer.MIN_VALUE, Integer.MIN_VALUE);
          return;
        }

        startPoint.setLocation(e.getPoint());
      }
    });

    source.addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {

        //
        // only on left left button && only if left button was clicked

        if (!SwingUtilities.isLeftMouseButton(e) || startPoint.x == Integer.MIN_VALUE || startPoint.y == Integer.MIN_VALUE) {
          return;
        }

        //
        // get location of the target

        int targetX = target.getLocation().x;
        int targetY = target.getLocation().y;

        //
        // determine how much the mouse moved from the start point

        int distanceX = (targetX + e.getX()) - (targetX + startPoint.x);
        int distanceY = (targetY + e.getY()) - (targetY + startPoint.y);

        //
        // the new position

        int newTargetX = targetX + distanceX;
        int newTargetY = targetY + distanceY;

        //
        // apply margins glue range (if set)

        if (marginsGlueRange > 0) {

          Rectangle sourceScreenDeviceBounds = source.getGraphicsConfiguration().getBounds();

          boolean b1, b2, b3, b4;

          //
          // left

          b1 = newTargetX - marginsGlueGaps.left < sourceScreenDeviceBounds.x + marginsGlueRange;
          b2 = newTargetX - marginsGlueGaps.left > sourceScreenDeviceBounds.x;

          b3 = newTargetX - marginsGlueGaps.left > sourceScreenDeviceBounds.x - marginsGlueRange;
          b4 = newTargetX - marginsGlueGaps.left < sourceScreenDeviceBounds.x;

          if ((b1 && b2) || (b3 && b4)) {
            newTargetX = sourceScreenDeviceBounds.x + marginsGlueGaps.left;
          }

          //
          // right

          else {

            b1 = newTargetX + target.getWidth() - marginsGlueGaps.right > sourceScreenDeviceBounds.x + sourceScreenDeviceBounds.width - marginsGlueRange;
            b2 = newTargetX + target.getWidth() - marginsGlueGaps.right < sourceScreenDeviceBounds.x + sourceScreenDeviceBounds.width;

            b3 = newTargetX + target.getWidth() - marginsGlueGaps.right < sourceScreenDeviceBounds.x + sourceScreenDeviceBounds.width + marginsGlueRange;
            b4 = newTargetX + target.getWidth() - marginsGlueGaps.right > sourceScreenDeviceBounds.x + sourceScreenDeviceBounds.width;

            if ((b1 && b2) || (b3 && b4)) {
              newTargetX = sourceScreenDeviceBounds.x + sourceScreenDeviceBounds.width - target.getWidth() + marginsGlueGaps.right;
            }
          }

          //
          // top

          b1 = newTargetY - marginsGlueGaps.top < sourceScreenDeviceBounds.y + marginsGlueRange;
          b2 = newTargetY - marginsGlueGaps.top > sourceScreenDeviceBounds.y;

          b3 = newTargetY - marginsGlueGaps.top > sourceScreenDeviceBounds.y - marginsGlueRange;
          b4 = newTargetY - marginsGlueGaps.top < sourceScreenDeviceBounds.y;

          if ((b1 && b2) || (b3 && b4)) {
            newTargetY = sourceScreenDeviceBounds.y + marginsGlueGaps.top;
          }

          //
          // bottom

          else {

            b1 = newTargetY + target.getHeight() - marginsGlueGaps.bottom > sourceScreenDeviceBounds.y + sourceScreenDeviceBounds.height - marginsGlueRange;
            b2 = newTargetY + target.getHeight() - marginsGlueGaps.bottom < sourceScreenDeviceBounds.y + sourceScreenDeviceBounds.height;

            b3 = newTargetY + target.getHeight() - marginsGlueGaps.bottom < sourceScreenDeviceBounds.y + sourceScreenDeviceBounds.height + marginsGlueRange;
            b4 = newTargetY + target.getHeight() - marginsGlueGaps.bottom > sourceScreenDeviceBounds.y + sourceScreenDeviceBounds.height;

            if ((b1 && b2) || (b3 && b4)) {
              newTargetY = sourceScreenDeviceBounds.y + sourceScreenDeviceBounds.height - target.getHeight() + marginsGlueGaps.bottom;
            }
          }
        }

        //
        // move the target to the new position

        target.setLocation(newTargetX, newTargetY);
      }
    });
  }

  //
  // --- ESCAPE
  //

  private static final KeyStroke ESCAPE_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
  private static final String ESCAPE_ACTION_MAP_KEY = "ESCAPE_ACTION_MAP_KEY";

  public static void setActionForEscapeKeyStroke(JComponent component, Action action) {
    component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ESCAPE_KEY_STROKE, ESCAPE_ACTION_MAP_KEY);
    component.getActionMap().put(ESCAPE_ACTION_MAP_KEY, action);
  }

  //
  // --- Ctrl + DOWN, LEFT, RIGHT UP
  //

  private static final KeyStroke CTRL_DOWN_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK);
  private static final String CTRL_DOWN_ACTION_MAP_KEY = "CTRL_DOWN_ACTION_MAP_KEY";

  private static final KeyStroke CTRL_LEFT_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK);
  private static final String CTRL_LEFT_ACTION_MAP_KEY = "CTRL_LEFT_ACTION_MAP_KEY";

  private static final KeyStroke CTRL_RIGHT_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.CTRL_DOWN_MASK);
  private static final String CTRL_RIGHT_ACTION_MAP_KEY = "CTRL_RIGHT_ACTION_MAP_KEY";

  private static final KeyStroke CTRL_UP_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK);
  private static final String CTRL_UP_ACTION_MAP_KEY = "CTRL_UP_ACTION_MAP_KEY";

  public static void setActionForCtrlDownKeyStroke(JComponent component, Action action) {
    component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(CTRL_DOWN_KEY_STROKE, CTRL_DOWN_ACTION_MAP_KEY);
    component.getActionMap().put(CTRL_DOWN_ACTION_MAP_KEY, action);
  }

  public static void setActionForCtrlLeftKeyStroke(JComponent component, Action action) {
    component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(CTRL_LEFT_KEY_STROKE, CTRL_LEFT_ACTION_MAP_KEY);
    component.getActionMap().put(CTRL_LEFT_ACTION_MAP_KEY, action);
  }

  public static void setActionForCtrlRightKeyStroke(JComponent component, Action action) {
    component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(CTRL_RIGHT_KEY_STROKE, CTRL_RIGHT_ACTION_MAP_KEY);
    component.getActionMap().put(CTRL_RIGHT_ACTION_MAP_KEY, action);
  }

  public static void setActionForCtrlUpKeyStroke(JComponent component, Action action) {
    component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(CTRL_UP_KEY_STROKE, CTRL_UP_ACTION_MAP_KEY);
    component.getActionMap().put(CTRL_UP_ACTION_MAP_KEY, action);
  }

  //
  // --- DOWN, LEFT, RIGHT UP
  //

  private static final KeyStroke DOWN_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
  private static final String DOWN_ACTION_MAP_KEY = "DOWN_ACTION_MAP_KEY";

  private static final KeyStroke LEFT_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
  private static final String LEFT_ACTION_MAP_KEY = "LEFT_ACTION_MAP_KEY";

  private static final KeyStroke RIGHT_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
  private static final String RIGHT_ACTION_MAP_KEY = "RIGHT_ACTION_MAP_KEY";

  private static final KeyStroke UP_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
  private static final String UP_ACTION_MAP_KEY = "UP_ACTION_MAP_KEY";

  public static void setActionForDownKeyStroke(JComponent component, Action action) {
    setActionForKeyStroke(component, action, DOWN_KEY_STROKE, DOWN_ACTION_MAP_KEY);
  }

  public static void setActionForLeftKeyStroke(JComponent component, Action action) {
    setActionForKeyStroke(component, action, LEFT_KEY_STROKE, LEFT_ACTION_MAP_KEY);
  }

  public static void setActionForRightKeyStroke(JComponent component, Action action) {
    setActionForKeyStroke(component, action, RIGHT_KEY_STROKE, RIGHT_ACTION_MAP_KEY);
  }

  public static void setActionForUpKeyStroke(JComponent component, Action action) {
    setActionForKeyStroke(component, action, UP_KEY_STROKE, UP_ACTION_MAP_KEY);
  }

  /**
   * Handy method to ease the set of actions for key strokes.
   */
  public static void setActionForKeyStroke(JComponent component, Action action, KeyStroke keyStroke, Object actionMapKey) {
    component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, actionMapKey);
    component.getActionMap().put(actionMapKey, action);
  }

  /**
   * Sets the location of the window relative to the associated screen. Gets the GraphicsConfiguration associated with
   * this Component
   * 
   * @param window
   *          the window to be moved
   */

  /**
   * @deprecated Replaced by {@link #centerWindowOnScreen(Window)}.
   */
  public static void setWindowLocationRelativeToScreen(Window window) {
    centerWindowOnScreen(window);
  }

  /**
   * Centers the window on the associated screen device.
   * 
   * @param window
   *          the window to be centered
   * @param screen
   *          the index of the screen device where the window should be centered
   */
  public static void centerWindowOnScreen(Window window) {
    centerWindowOnScreen(window, window.getGraphicsConfiguration());
  }

  /**
   * Centers the window on a screen device (specified as an index).
   * 
   * @param window
   *          the window to be centered
   * @param screen
   *          the index of the screen device where the window should be centered
   */
  public static void centerWindowOnScreen(Window window, int screen) {
    centerWindowOnScreen(window, GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screen].getDefaultConfiguration());
  }

  /**
   * Centers the window on a screen device (specified as a {@link GraphicsConfiguration}).
   * 
   * @param window
   *          the window to be centered
   * @param gc
   *          the {@link GraphicsConfiguration} of the screen device where the window should be centered
   */
  public static void centerWindowOnScreen(Window window, GraphicsConfiguration gc) {

    Rectangle screenBounds = gc.getBounds();
    Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

    int width = window.getWidth();
    int height = window.getHeight();

    int x = screenBounds.x + (screenBounds.width + screenInsets.left + screenInsets.right - width) / 2;
    int y = screenBounds.y + (screenBounds.height + screenInsets.top + screenInsets.bottom - height) / 2;

    window.setLocation(x, y);
  }

  public static void centerWindowOnScreen(Window window, int widthPercent, int heightPercent) {
    centerWindowOnScreen(window, window.getGraphicsConfiguration(), widthPercent, heightPercent);
  }

  public static void centerWindowOnScreen(Window window, int screen, int widthPercent, int heightPercent) {
    centerWindowOnScreen(window, GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screen].getDefaultConfiguration(), widthPercent, heightPercent);
  }

  public static void centerWindowOnScreen(Window window, GraphicsConfiguration gc, int widthPercent, int heightPercent) {

    Rectangle screenBounds = gc.getBounds();
    Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

    int width = (int) (screenBounds.width * (widthPercent / 100d));
    int height = (int) (screenBounds.height * (heightPercent / 100d));

    int x = screenBounds.x + (screenBounds.width + screenInsets.left + screenInsets.right - width) / 2;
    int y = screenBounds.y + (screenBounds.height + screenInsets.top + screenInsets.bottom - height) / 2;

    window.setBounds(x, y, width, height);
  }

  /**
   * Sets the location of the window relative to the specified component.
   * 
   * @param window
   *          the window to be moved
   * @param component
   *          the component in relation to which the window's location is determined
   */
  public static void setWindowLocationRelativeToComponent(Window window, Component component) {
    window.setLocationRelativeTo(component);
  }

  public static boolean isVisibleOnTheScreen(Rectangle rectangle) {

    Rectangle bounds = new Rectangle(0, 0, 0, 0);

    for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
      bounds.add(gd.getDefaultConfiguration().getBounds());
    }

    return bounds.contains(rectangle.getBounds());
  }

  public static boolean isVisibleOnTheScreen(Component component) {
    return isVisibleOnTheScreen(component.getBounds());
  }

  /**
   * Adds to the provided text component a focus listener that will select all the text on focus gained.
   * 
   * @return the focus listener
   */
  public static FocusListener setSelectAllOnFocus(final JTextComponent textComponent) {

    FocusListener focusListener = new FocusAdapter() {

      @Override
      public void focusGained(FocusEvent e) {
        textComponent.select(0, textComponent.getText().length());
      }
    };

    textComponent.addFocusListener(focusListener);

    return focusListener;
  }

  /**
   * @see #highlight(JTextComponent, String, HighlightPainter, boolean)
   */
  public static void highlight(JTextComponent textComponent, String pattern, Color color) {
    highlight(textComponent, pattern, new DefaultHighlighter.DefaultHighlightPainter(color), false);
  }

  /**
   * @see #highlight(JTextComponent, String, HighlightPainter, boolean)
   */
  public static void highlight(JTextComponent textComponent, String pattern, HighlightPainter highlightPainter) {
    highlight(textComponent, pattern, highlightPainter, false);
  }

  /**
   * @see #highlight(JTextComponent, String, HighlightPainter, boolean)
   */
  public static void highlight(JTextComponent textComponent, String pattern, Color color, boolean ignoreCase) {
    highlight(textComponent, pattern, new DefaultHighlighter.DefaultHighlightPainter(color), ignoreCase);
  }

  /**
   * Highlights the patterns found in the text of the provided {@link JTextComponent}.
   * 
   * @param textComponent
   * @param pattern
   * @param highlightPainter
   * @param ignoreCase
   */
  public static void highlight(JTextComponent textComponent, String pattern, HighlightPainter highlightPainter, boolean ignoreCase) {

    String text = textComponent.getText();
    Highlighter highlighter = textComponent.getHighlighter();

    highlighter.removeAllHighlights();

    if (pattern.length() == 0) {
      return;
    }

    if (ignoreCase) {
      text = text.toLowerCase();
      pattern = pattern.toLowerCase();
    }

    int fromIndex = 0;

    while (true) {

      int index = text.indexOf(pattern, fromIndex);

      if (index < 0) {
        break;
      }

      int p0 = index;
      int p1 = index + pattern.length();

      try {

        if (p1 == text.length()) {
          highlighter.addHighlight(p0, p1 - 1, highlightPainter);
        }

        else {
          highlighter.addHighlight(p0, p1, highlightPainter);
        }
      }

      catch (BadLocationException e) {
        throw new RuntimeException("unexpected exception", e);
      }

      fromIndex = p1;
    }
  }

  /**
   * Loads the native {@link LookAndFeel}.
   */
  public static void setSystemLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
      logger.error("failed to set the native system look and feel", e);
    }
  }

  /**
   * Loads the default cross platform {@link LookAndFeel}.
   */
  public static void setCrossPlatformLookAndFeels() {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
      logger.error("failed to set the default cross platform look and feel", e);
    }
  }

  /**
   * Disables the decorations for the specified frame.
   */
  public static void disableDecorations(JFrame frame) {

    synchronized (frame) {

      boolean frameIsVisible = frame.isVisible();
      JRootPane frameRootPane = frame.getRootPane();

      Point frameRootPaneLocation = null;
      Dimension frameRootPaneSize = null;
      if (frameIsVisible) {
        frameRootPaneLocation = frameRootPane.getLocationOnScreen();
        frameRootPaneSize = frameRootPane.getSize();
      }

      if (frame.isDisplayable()) {
        frame.dispose();
      }

      frame.setUndecorated(true);
      frame.setBackground(new Color(1f, 1f, 1f, 0f));

      if (frameIsVisible) {

        frame.setLocation(frameRootPaneLocation);
        frame.setSize(frameRootPaneSize);

        frame.setVisible(true);
      }
    }
  }

  /**
   * Enables the decorations for the specified frame.
   */
  public static void enableDecorations(JFrame frame) {

    synchronized (frame) {

      boolean frameIsVisible = frame.isVisible();
      JRootPane frameRootPane = frame.getRootPane();

      Point frameRootPaneLocationBefore = null;
      Dimension frameRootPaneSizeBefore = null;
      if (frameIsVisible) {
        frameRootPaneLocationBefore = frameRootPane.getLocationOnScreen();
        frameRootPaneSizeBefore = frameRootPane.getSize();
      }

      if (frame.isDisplayable()) {
        frame.dispose();
      }

      frame.setBackground(null);
      frame.setUndecorated(false);

      if (frameIsVisible) {

        frame.setVisible(true);

        Point frameRootPaneLocationAfter = frameRootPane.getLocationOnScreen();
        Dimension frameRootPaneSizeAfter = frameRootPane.getSize();

        Point frameLocation = frame.getLocationOnScreen();
        Dimension frameSize = frame.getSize();

        frame.setLocation(frameLocation.x - (frameRootPaneLocationAfter.x - frameRootPaneLocationBefore.x), frameLocation.y - (frameRootPaneLocationAfter.y - frameRootPaneLocationBefore.y));
        frame.setSize(frameSize.width + (frameRootPaneSizeBefore.width - frameRootPaneSizeAfter.width), frameSize.height + (frameRootPaneSizeBefore.height - frameRootPaneSizeAfter.height));
      }
    }
  }

  public static int getScreenWidth() {
    return getScreenSize().width;
  }

  public static int getScreenHeight() {
    return getScreenSize().height;
  }

  public static Dimension getScreenSize() {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }

  public static int getScreenWidth(Window window) {
    return window.getGraphicsConfiguration().getDevice().getDefaultConfiguration().getBounds().width;
  }

  public static int getScreenHeight(final Window window) {
    return window.getGraphicsConfiguration().getDevice().getDefaultConfiguration().getBounds().width;
  }

  public static Dimension getScreenSize(final Window window) {
    return window.getGraphicsConfiguration().getDevice().getDefaultConfiguration().getBounds().getSize();
  }

  public static int getSmallestScreenWidth() {

    int smallestScreenWidth = Integer.MAX_VALUE;

    for (GraphicsDevice graphicsDevice : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
      smallestScreenWidth = Math.min(smallestScreenWidth, graphicsDevice.getDisplayMode().getWidth());
    }

    return smallestScreenWidth;
  }

  public static int getSmallestScreenHeight() {

    int smallestScreenHeight = Integer.MAX_VALUE;

    for (GraphicsDevice graphicsDevice : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
      smallestScreenHeight = Math.min(smallestScreenHeight, graphicsDevice.getDisplayMode().getHeight());
    }

    return smallestScreenHeight;
  }

  public static int getBiggestScreenWidth() {

    int biggestScreenWidth = Integer.MIN_VALUE;

    for (GraphicsDevice graphicsDevice : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
      biggestScreenWidth = Math.max(biggestScreenWidth, graphicsDevice.getDisplayMode().getWidth());
    }

    return biggestScreenWidth;
  }

  public static int getBiggestScreenHeight() {

    int biggestScreenHeight = Integer.MIN_VALUE;

    for (GraphicsDevice graphicsDevice : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
      biggestScreenHeight = Math.max(biggestScreenHeight, graphicsDevice.getDisplayMode().getHeight());
    }

    return biggestScreenHeight;
  }

}
