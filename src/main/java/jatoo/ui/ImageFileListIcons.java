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

public class ImageFileListIcons extends Thread {

  private final ImageThumbnails thumbnails = new ImageThumbnails();

  private final LinkedList<File> files = new LinkedList<>();
  private final Map<File, Icon> icons = new HashMap<>();

  private final ImageFileList list;

  private boolean skipCycle;

  public ImageFileListIcons(final ImageFileList list) {
    this.list = list;
    start();
  }

  public void add(final File file) {
    synchronized (this.files) {
      this.files.add(file);
      this.files.notify();
    }
  }

  public void add(final List<File> files) {
    synchronized (this.files) {
      this.files.addAll(files);
      this.files.notify();
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

  public Icon getIcon(File file) {
    synchronized (this.icons) {
      return icons.get(file);
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
          icon = ImageFileList.ICON_ERROR;
        }
      }

      else {
        icon = ImageFileList.ICON_ERROR;
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
