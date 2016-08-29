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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
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
 * @version 3.0, May 20, 2014
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
   * Sets the location of the window relative to the associated screen.
   * 
   * @param window
   *          the window to be moved
   */
  public static void setWindowLocationRelativeToScreen(Window window) {

    GraphicsConfiguration gc = window.getGraphicsConfiguration();
    Rectangle gcBounds = gc.getBounds();

    Dimension windowSize = window.getSize();

    int dx = gcBounds.x + (gcBounds.width - windowSize.width) / 2;
    int dy = gcBounds.y + (gcBounds.height - windowSize.height) / 2;

    window.setLocation(dx, dy);
  }

  /**
   * Sets the location of the window relative to the specified component.
   * 
   * @param window
   *          the window to be moved
   * @param component
   *          the component in relation to which the window's location is
   *          determined
   */
  public static void setWindowLocationRelativeToComponent(Window window, Component component) {
    window.setLocationRelativeTo(component);
  }

  /**
   * Adds to the provided text component a focus listener that will select all
   * the text on focus gained.
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
   * Highlights the patterns found in the text of the provided
   * {@link JTextComponent}.
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

}
