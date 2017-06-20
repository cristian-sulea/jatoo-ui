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

import java.awt.Image;

import javax.swing.ImageIcon;

import jatoo.resources.ResourcesImages;
import jatoo.resources.ResourcesTexts;

/**
 * This class provides the resources for the UI components.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.0-SNAPSHOT, June 20, 2017
 */
public class UITheme2 {

  private static final ResourcesTexts DEFAULT_TEXTS = new ResourcesTexts(UITheme2.class);
  private static final ResourcesImages DEFAULT_IMAGES = new ResourcesImages(UITheme2.class);

  private static ResourcesTexts texts = DEFAULT_TEXTS;
  private static ResourcesImages images = DEFAULT_IMAGES;

  public final static synchronized void setTheme(Class<?> clazz) {
    texts = new ResourcesTexts(clazz, DEFAULT_TEXTS);
    images = new ResourcesImages(clazz, DEFAULT_IMAGES);
  }

  /**
   * {@link ResourcesTexts#getText(String)}
   */
  public final static synchronized String getText(String key) {
    return texts.getText(key);
  }

  /**
   * {@link ResourcesTexts#getText(String, Object...)}
   */
  public final static synchronized String getText(String key, Object... arguments) {
    return texts.getText(key, arguments);
  }

  /**
   * {@link ResourcesImages#getImage(String)}
   */
  public final static synchronized Image getImage(String name) {
    return images.getImage(name);
  }

  /**
   * {@link ResourcesImages#getImageIcon(String)}
   */
  public final static synchronized ImageIcon getImageIcon(String name) {
    return images.getImageIcon(name);
  }

}
