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
 * @version 1.1, January 17, 2018
 */
public class UIResources {

  private static final ResourcesTexts DEFAULT_TEXTS = new ResourcesTexts(UIResources.class);
  private static final ResourcesImages DEFAULT_IMAGES = new ResourcesImages(UIResources.class);

  private static final Map<Class<?>, ResourcesTexts> CACHED_TEXTS = new HashMap<>();
  private static final Map<Class<?>, ResourcesImages> CACHED_IMAGES = new HashMap<>();

  private static Class<?> RESOURCES_BASE_CLASS = UIResources.class;

  public final static synchronized void setResourcesBaseClass(final Class<?> resourcesBaseClass) {

    RESOURCES_BASE_CLASS = resourcesBaseClass;

    CACHED_TEXTS.clear();
    CACHED_IMAGES.clear();
  }

  /**
   * {@link ResourcesTexts#getText(String)}
   */
  public final static synchronized String getText(String key) {
    return getResourcesTexts(RESOURCES_BASE_CLASS).getText(key);
  }

  /**
   * {@link ResourcesTexts#getText(String, Object...)}
   */
  public final static synchronized String getText(String key, Object... arguments) {
    return getResourcesTexts(RESOURCES_BASE_CLASS).getText(key, arguments);
  }

  /**
   * {@link ResourcesImages#getImage(String)}
   */
  public final static synchronized Image getImage(String name) {
    return getResourcesImages(RESOURCES_BASE_CLASS).getImage(name);
  }

  /**
   * {@link ResourcesImages#getImageIcon(String)}
   */
  public final static synchronized ImageIcon getImageIcon(String name) {
    return getResourcesImages(RESOURCES_BASE_CLASS).getImageIcon(name);
  }

  //
  // ---
  //

  private static ResourcesTexts getResourcesTexts(Class<?> clazz) {
    ResourcesTexts texts = CACHED_TEXTS.get(clazz);
    if (texts == null) {
      texts = new ResourcesTexts(clazz, DEFAULT_TEXTS);
      CACHED_TEXTS.put(clazz, texts);
    }
    return texts;
  }

  private static ResourcesImages getResourcesImages(Class<?> clazz) {
    ResourcesImages images = CACHED_IMAGES.get(clazz);
    if (images == null) {
      images = new ResourcesImages(clazz, DEFAULT_IMAGES);
      CACHED_IMAGES.put(clazz, images);
    }
    return images;
  }

}
