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

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * This class provides the resources for the UI components.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.0-SNAPSHOT, July 31, 2014
 */
public class UITheme {

  private static ResourceBundle texts;

  private static Cursor cursorDefault;
  private static Cursor cursorDrag;
  private static Cursor cursorDragging;

  private static Map<String, Icon> icons = new HashMap<>();

  private static Map<String, BufferedImage> images = new HashMap<>();

  static {

    texts = ResourceBundle.getBundle(UITheme.class.getPackage().getName() + ".texts");

    cursorDefault = Cursor.getDefaultCursor();
    cursorDrag = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(UITheme.class.getResource("CursorDrag.gif")).getImage(), new Point(0, 0), "CursorDrag");
    cursorDragging = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(UITheme.class.getResource("CursorDragging.gif")).getImage(), new Point(0, 0), "CursorDragging");

    initIcon(ImageFileList.class, "AddImages", "png");
    initIcon(ImageFileList.class, "RemoveAll", "png");
    initIcon(ImageFileList.class, "RemoveSelected", "png");

    initIcon(WorkerWithProgressDialog.class, "Icon", "png");

    initImage(ImageFileListCellRenderer.class, "DefaultImage", "png");
  }

  private static void initIcon(Class<?> clazz, String name, String extension) {
    setIcon(clazz, name, new ImageIcon(ImageFileList.class.getResource(clazz.getSimpleName() + "." + name + "." + extension)));
  }

  private static void initImage(Class<?> clazz, String name, String extension) {
    try {
      setImage(clazz, name, ImageUtils.read(clazz.getResource(clazz.getSimpleName() + "." + name + "." + extension)));
    } catch (IOException e) {
      throw new MissingResourceException("image is missing from the package", clazz.getName(), clazz.getSimpleName() + "." + name + "." + extension);
    }
  }

  public static final String getText(Class<?> clazz, String name) {
    return texts.getString(clazz.getSimpleName() + "." + name);
  }

  public static final void setTexts(ResourceBundle texts) {
    UITheme.texts = texts;
  }

  public static final Cursor getCursorDefault() {
    return cursorDefault;
  }

  public static final void setCursorDefault(Cursor cursorDefault) {
    UITheme.cursorDefault = cursorDefault;
  }

  public static final Cursor getCursorDrag() {
    return cursorDrag;
  }

  public static final void setCursorDrag(Cursor cursorDrag) {
    UITheme.cursorDrag = cursorDrag;
  }

  public static final Cursor getCursorDragging() {
    return cursorDragging;
  }

  public static final void setCursorDragging(Cursor cursorDragging) {
    UITheme.cursorDragging = cursorDragging;
  }

  public static final Icon getIcon(Class<?> clazz, String name) {
    return icons.get(clazz.getName() + "." + name);
  }

  public static final void setIcon(Class<?> clazz, String name, Icon icon) {
    icons.put(clazz.getName() + "." + name, icon);
  }

  public static final BufferedImage getImage(Class<?> clazz, String name) {
    return images.get(clazz.getName() + "." + name);
  }

  public static final void setImage(Class<?> clazz, String name, BufferedImage image) {
    images.put(clazz.getName() + "." + name, image);
  }

}
