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

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import jatoo.image.ImageFileFilter;
import jatoo.image.ImageThumbnails;
import jatoo.image.ImageUtils;

/**
 * The icons loader for {@link ImageFileList} component.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.0-SNAPSHOT, June 15, 2017
 */
public class ImageFileListIcons extends Thread {

  public static final ImageIcon ICON_ERROR = new ImageIcon();

  private final ImageThumbnails thumbnails = new ImageThumbnails();

  private final LinkedList<File> files = new LinkedList<>();
  private final Map<File, Icon> icons = new HashMap<>();

  private final ImageFileList list;

  private boolean skipCycle;

  public ImageFileListIcons(final ImageFileList list) {
    this.list = list;
    start();
  }

  public void addToLoadingQueue(final File file) {
    synchronized (this.files) {
      this.files.add(file);
      this.files.notify();
    }
  }

  public void addToLoadingQueue(final List<File> files) {
    synchronized (this.files) {
      this.files.addAll(files);
      this.files.notify();
    }
  }

  public Icon get(File file) {
    synchronized (this.icons) {
      return icons.get(file);
    }
  }

  public void clear() {

    this.skipCycle = true;

    synchronized (this.icons) {
      this.icons.clear();
    }

    synchronized (this.files) {
      this.files.clear();
      this.files.notify();
    }
  }

  public void run() {

    while (true) {

      skipCycle = false;

      File file;

      synchronized (files) {

        if (files.isEmpty()) {

          try {
            files.wait();
          } catch (InterruptedException e) {}

          continue;
        }

        file = files.removeFirst();
      }

      final ImageIcon icon;

      if (ImageFileFilter.getInstance().accept(file)) {

        BufferedImage image = thumbnails.get(file, list.getIconSize(), list.getIconSize());

        if (image != null) {

          if (list.isIconShadow()) {
            image = ImageUtils.addShadow(image);
          }

          icon = new ImageIcon(image);
        }

        else {
          icon = ImageFileListIcons.ICON_ERROR;
        }
      }

      else {
        icon = ImageFileListIcons.ICON_ERROR;
      }

      if (skipCycle) {
        continue;
      }

      synchronized (icons) {
        icons.put(file, icon);
      }

      list.fireContentsChanged();
    }
  }

}
