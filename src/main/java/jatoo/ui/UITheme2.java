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
import java.util.HashMap;
import java.util.Map;

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

  private static final Map<Class<?>, ResourcesTexts> CACHE_TEXTS = new HashMap<>();
  private static final Map<Class<?>, ResourcesImages> CACHE_IMAGES = new HashMap<>();

  /**
   * {@link ResourcesTexts#getText(String)}
   */
  public final static synchronized String getText(Class<?> clazz, String key) {
    return getResourcesTexts(clazz).getText(key);
  }

  /**
   * {@link ResourcesTexts#getText(String, Object...)}
   */
  public final static synchronized String getText(Class<?> clazz, String key, Object... arguments) {
    return getResourcesTexts(clazz).getText(key, arguments);
  }

  /**
   * {@link ResourcesImages#getImage(String)}
   */
  public final static synchronized Image getImage(Class<?> clazz, String name) {
    return getResourcesImages(clazz).getImage(name);
  }

  /**
   * {@link ResourcesImages#getImageIcon(String)}
   */
  public final static synchronized ImageIcon getImageIcon(Class<?> clazz, String name) {
    return getResourcesImages(clazz).getImageIcon(name);
  }

  private static ResourcesTexts getResourcesTexts(Class<?> clazz) {
    ResourcesTexts texts = CACHE_TEXTS.get(clazz);
    if (texts == null) {
      texts = new ResourcesTexts(clazz, DEFAULT_TEXTS);
      CACHE_TEXTS.put(clazz, texts);
    }
    return texts;
  }

  private static ResourcesImages getResourcesImages(Class<?> clazz) {
    ResourcesImages images = CACHE_IMAGES.get(clazz);
    if (images == null) {
      images = new ResourcesImages(clazz, DEFAULT_IMAGES);
      CACHE_IMAGES.put(clazz, images);
    }
    return images;
  }

}
