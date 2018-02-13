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
import java.awt.Image;
import java.awt.Point;
import java.awt.Window;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import javax.swing.WindowConstants;

import jatoo.properties.FileProperties;

/**
 * A generic base class created to ease the work with java window applications, like {@link JFrame} and {@link JDialog}.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 3.1, February 9, 2018
 */
public abstract class AppWindow {

  private final Window window;
  private final FileProperties properties;

  protected AppWindow(Window window) {
    this.window = window;

    //
    // properties

    properties = new FileProperties(new File(System.getProperty("user.home"), getClass().getName() + ".properties"));
    properties.loadSilently();

    //
    // add shutdown hook to save properties

    // Runtime.getRuntime().addShutdownHook(new Thread() {
    // public void run() {}
    // });

    //
    // icons

    List<Image> icons = new ArrayList<>();

    for (String size : new String[] { "016", "020", "022", "024", "032", "048", "064", "128", "256", "512" }) {
      try {
        icons.add(new ImageIcon(getClass().getResource("icon-" + size + ".png")).getImage());
      } catch (Exception e) {}
    }

    if (icons.size() > 0) {
      window.setIconImages(icons);
    }

    //
    // location & size

    window.setLocation(properties.getPropertyAsPoint("location", new Point(Integer.MIN_VALUE, Integer.MIN_VALUE)));
    window.setSize(properties.getPropertyAsDimension("size", new Dimension(Integer.MIN_VALUE, Integer.MIN_VALUE)));

    if (!UIUtils.isVisibleOnTheScreen(window)) {
      UIUtils.centerWindowOnScreen(window, 50, 50);
    }

    //
    // default close operation is DISPOSE

    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  Window getWindow() {
    return window;
  }

  public abstract void setTitle(String title);

  public abstract void setDefaultCloseOperation(int operation);

  public void saveProperties() {

    properties.setProperty("location", window.getLocation());
    properties.setProperty("size", window.getSize());

    properties.saveSilently();
  }

  /**
   * @see JRootPane#setContentPane(Container)
   */
  public void setContentPane(JComponent contentPane) {
    ((RootPaneContainer) window).getRootPane().setContentPane(contentPane);
  }

  /**
   * @see JRootPane#getContentPane()
   */
  public JComponent getContentPane() {
    return (JComponent) ((RootPaneContainer) window).getRootPane().getContentPane();
  }

  /**
   * @see java.awt.Window#setVisible(boolean)
   */
  public void setVisible(boolean b) {
    window.setVisible(b);
  }

  /**
   * @see java.awt.Window#dispose()
   */
  public void dispose() {
    window.dispose();
  }

  /**
   * @see DropTarget
   */
  public void addDropTargetListener(DropTargetListener listener) {
    new DropTarget(window, listener);
  }

  /**
   * @see JOptionPane#showConfirmDialog(java.awt.Component, Object, String, int, int, javax.swing.Icon)
   */
  public boolean showConfirmationWarning(String title, String message) {
    return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(window, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
  }

}
