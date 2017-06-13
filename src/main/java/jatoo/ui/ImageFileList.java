/*
 * Copyright (C) Cristian Sulea ( http://cristian.sulea.net )
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatoo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;

import jatoo.image.ImageFileFilter;
import jatoo.image.ImageThumbnails;
import jatoo.image.ImageUtils;
import jatoo.resources.ResourcesImages;
import net.miginfocom.swing.MigLayout;

/**
 * A component that displays a list of images from {@link File}s.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 2.0-SNAPSHOT, June 13, 2017
 */
@SuppressWarnings("serial")
public class ImageFileList extends JPanel {

  public static final ImageIcon ICON_ERROR = new ImageIcon();

  private ResourcesImages resourcesImages = new ResourcesImages(getClass());

  private final JList<File> list;
  private final ImageFileListModel model;
  private final ImageFileListCellRenderer renderer;

  private final JScrollPane listScrollPane;

  private int iconSize;
  private boolean iconShadow;

  private int itemSpace;

  private final Map<File, Icon> icons = new HashMap<>();
  private IconsLoader iconsLoader = new IconsLoader();

  public ImageFileList() {
    this(3, 4);
  }

  public ImageFileList(final int rows, final int columns) {
    this(rows, columns, 100, 5);
  }

  public ImageFileList(final int rows, final int columns, final int iconSize, final int itemSpace) {
    this(rows, columns, iconSize, itemSpace, true);
  }

  public ImageFileList(final int rows, final int columns, final int iconSize, final int itemSpace, final boolean addButtons) {
    this(rows, columns, iconSize, itemSpace, addButtons, true);
  }

  public ImageFileList(final int rows, final int columns, final int iconSize, final int itemSpace, final boolean addButtons, final boolean iconShadow) {

    this.iconSize = iconSize;
    this.iconShadow = iconShadow;

    this.itemSpace = itemSpace;

    //
    // model and cell renderer

    final int listScrollableUnitIncrement = Math.max(9, iconSize / 10);

    list = new JList<File>() {
      @Override
      public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (listScrollableUnitIncrement > 9) {
          return listScrollableUnitIncrement;
        } else {
          return super.getScrollableUnitIncrement(visibleRect, orientation, direction);
        }
      }
    };
    list.setModel(model = new ImageFileListModel());
    list.setCellRenderer(renderer = new ImageFileListCellRenderer(this));

    list.setBorder(BorderFactory.createEmptyBorder(0, 0, itemSpace, itemSpace));

    //
    // dummy files for the scroll pane (viewport) preferred size

    list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    list.setVisibleRowCount(rows);

    for (int i = 1; i <= (rows * columns - 1); i++) {
      model.addImage(new File(""));
    }

    Dimension viewportPreferredSize = list.getPreferredSize();

    model.removeAllImages();

    list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    list.setVisibleRowCount(-1);

    //
    // actions

    final Action addImagesAction = new AbstractAction(UITheme.getText(ImageFileList.class, "AddImages"), UITheme.getIcon(ImageFileList.class, "AddImages")) {
      public void actionPerformed(ActionEvent e) {

        final FileChooser fc = new FileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileChooserFileFilter("Images", ImageUtils.getFormatNames()));

        if (fc.showOpenDialog(ImageFileList.this) == JFileChooser.APPROVE_OPTION) {
          addImages(Arrays.asList(fc.getSelectedFiles()));
        }
      }
    };

    final Action removeSelectedAction = new AbstractAction(UITheme.getText(ImageFileList.class, "RemoveSelected"), UITheme.getIcon(ImageFileList.class, "RemoveSelected")) {
      public void actionPerformed(ActionEvent e) {
        removeSelectedImages();
      }
    };

    final Action removeAllAction = new AbstractAction(UITheme.getText(ImageFileList.class, "RemoveAll"), UITheme.getIcon(ImageFileList.class, "RemoveAll")) {
      public void actionPerformed(ActionEvent e) {
        removeAllImages();
      }
    };

    list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "removeSelectedImagesActionMapKey");
    list.getActionMap().put("removeSelectedImagesActionMapKey", removeSelectedAction);

    //
    // list container

    listScrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    listScrollPane.getViewport().setPreferredSize(viewportPreferredSize);

    //
    // layout

    if (addButtons) {
      setLayout(new MigLayout("insets 0", "[fill][fill]push[fill]", "[fill, grow][fill]"));
      add(listScrollPane, "spanx");
      add(new Button(addImagesAction));
      add(new Button(removeSelectedAction));
      add(new Button(removeAllAction));
    }

    else {
      setLayout(new GridLayout());
      add(listScrollPane);
    }
  }

  public ResourcesImages getResourcesImages() {
    return resourcesImages;
  }

  public void setIconSize(int iconSize) {
    setIconStyle(iconSize, iconShadow);
  }

  public int getIconSize() {
    return iconSize;
  }

  public void setIconShadow(boolean iconShadow) {
    setIconStyle(iconSize, iconShadow);
  }

  public boolean isIconShadow() {
    return iconShadow;
  }

  public void setItemSpace(int itemSpace) {

    this.itemSpace = itemSpace;

    list.setBorder(BorderFactory.createEmptyBorder(0, 0, itemSpace, itemSpace));
    renderer.fireItemSpaceChanged();
    model.fireContentsChanged();
  }

  public int getItemSpace() {
    return itemSpace;
  }

  public Icon getIcon(File file) {
    synchronized (ImageFileList.this) {
      return icons.get(file);
    }
  }

  public void addImage(final File file) {
    model.addImage(file);
    iconsLoader.add(file);
  }

  public void addImages(final List<File> files) {
    model.addImages(files);
    iconsLoader.add(files);
  }

  public File getSelectedImage() {
    return list.getSelectedValue();
  }

  public List<File> getSelectedImages() {
    return list.getSelectedValuesList();
  }

  public List<File> getImages() {
    return model.getImages();
  }

  public void removeImage(final File file) {
    model.removeImage(file);
  }

  public void removeImages(final List<File> files) {
    model.removeImages(files);
  }

  public void removeSelectedImages() {
    int index = list.getSelectedIndex();
    model.removeImages(getSelectedImages());
    if (index >= model.getSize()) {
      index = model.getSize() - 1;
    }
    list.setSelectedIndex(index);
  }

  public void removeAllImages() {
    model.removeAllImages();
  }

  public int getImagesCount() {
    return model.size();
  }

  public final void selectPrevImage() {

    int index = list.getSelectedIndex();

    if (index == -1) {
      index = 0;
    }

    else {

      index--;

      if (index < 0) {
        index = model.getSize() - 1;
      }
    }

    list.setSelectedIndex(index);
  }

  public final void selectNextImage() {

    int index = list.getSelectedIndex();

    if (index == -1) {
      index = 0;
    }

    else {

      index++;

      if (index >= model.getSize()) {
        index = 0;
      }
    }

    list.setSelectedIndex(index);
  }

  public void setListBorder(final Border border) {
    listScrollPane.setBorder(border);
  }

  public Border getListBorder() {
    return listScrollPane.getBorder();
  }

  public void setListBackground(final Color background) {
    list.setBackground(background);
  }

  public Color getListBackground() {
    return list.getBackground();
  }

  public synchronized void addListSelectionListener(final ListSelectionListener listener) {
    list.addListSelectionListener(listener);
  }

  public synchronized void removeListSelectionListener(final ListSelectionListener listener) {
    list.removeListSelectionListener(listener);
  }

  public synchronized void addListMouseListener(final MouseListener listener) {
    list.addMouseListener(listener);
  }

  public synchronized void removeListMouseListener(final MouseListener listener) {
    list.removeMouseListener(listener);
  }

  public synchronized void addListMouseMotionListener(final MouseMotionListener listener) {
    list.addMouseMotionListener(listener);
  }

  public synchronized void removeListMouseMotionListener(final MouseMotionListener listener) {
    list.removeMouseMotionListener(listener);
  }

  public synchronized void addListMouseWheelListener(final MouseWheelListener listener) {
    list.addMouseWheelListener(listener);
  }

  public synchronized void removeListMouseWheelListener(final MouseWheelListener listener) {
    list.removeMouseWheelListener(listener);
  }

  //
  // ---
  //

  private void setIconStyle(final int iconSize, final boolean iconShadow) {

    synchronized (ImageFileList.this) {

      this.iconSize = iconSize;
      this.iconShadow = iconShadow;

      iconsLoader.clear();

      icons.clear();

      renderer.fireIconStyleChanged();
      model.fireContentsChanged();

      iconsLoader.add(getImages());
    }
  }

  public class IconsLoader extends Thread {

    private final ImageThumbnails thumbnails = new ImageThumbnails();
    private final LinkedList<File> files = new LinkedList<>();

    private boolean skipCycle;

    public IconsLoader() {
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
      synchronized (this.files) {
        this.files.clear();
        this.files.notify();
        this.skipCycle = true;
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

          BufferedImage image = thumbnails.get(file, iconSize, iconSize);

          if (image != null) {

            if (iconShadow) {
              image = ImageUtils.addShadow(image);
            }

            icon = new ImageIcon(image);
          }

          else {
            icon = ICON_ERROR;
          }
        }

        else {
          icon = ICON_ERROR;
        }

        synchronized (ImageFileList.this) {

          if (skipCycle) {
            continue;
          }

          icons.put(file, icon);
        }

        model.fireContentsChanged();
      }
    }
  }

}
