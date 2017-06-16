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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;

import jatoo.image.ImageUtils;
import jatoo.resources.ResourcesImages;
import net.miginfocom.swing.MigLayout;

/**
 * A component that displays a list of images from {@link File}s.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 2.0-SNAPSHOT, June 16, 2017
 */
@SuppressWarnings("serial")
public class ImageFileList extends JPanel {

  private ResourcesImages resourcesImages = new ResourcesImages(getClass());

  private final JList<File> list;

  private final ImageFileListIcons icons;
  private final ImageFileListModel model;
  private final ImageFileListCellRenderer renderer;

  private final JScrollPane scrollPane;

  private int iconSize;
  private boolean iconShadow;

  private int itemSpace;

  private final boolean isInitializationDone;

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
    // init list

    icons = new ImageFileListIcons(this);
    model = new ImageFileListModel();
    renderer = new ImageFileListCellRenderer(this, icons);

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
    list.setModel(model);
    list.setCellRenderer(renderer);

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

    scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.getViewport().setPreferredSize(viewportPreferredSize);

    //
    // the method is overridden so use "super" to nullify the border

    super.setBorder(null);

    //
    // layout

    if (addButtons) {
      setLayout(new MigLayout("insets 0", "[fill][fill]push[fill]", "[fill, grow][fill]"));
      add(scrollPane, "spanx");
      add(new Button(addImagesAction));
      add(new Button(removeSelectedAction));
      add(new Button(removeAllAction));
    }

    else {
      setLayout(new GridLayout());
      add(scrollPane);
    }

    //
    //

    isInitializationDone = true;
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

  public void addImage(final File file) {
    model.addImage(file);
    icons.addToLoadingQueue(file);
  }

  public void addImages(final List<File> files) {
    model.addImages(files);
    icons.addToLoadingQueue(files);
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

  //
  // --- delegate methods
  //

  public void fireContentsChanged() {
    model.fireContentsChanged();
  }

  @Override
  public void setBorder(Border border) {
    if (isInitializationDone) {
      scrollPane.setBorder(border);
    } else {
      super.setBorder(border);
    }
  }

  @Override
  public Border getBorder() {
    if (isInitializationDone) {
      return scrollPane.getBorder();
    } else {
      return super.getBorder();
    }
  }

  @Override
  public void setBackground(Color bg) {
    if (isInitializationDone) {
      list.setBackground(bg);
    } else {
      super.setBackground(bg);
    }
  }

  @Override
  public Color getBackground() {
    if (isInitializationDone) {
      return list.getBackground();
    } else {
      return super.getBackground();
    }
  }

  public synchronized void addListSelectionListener(final ListSelectionListener listener) {
    list.addListSelectionListener(listener);
  }

  public synchronized void removeListSelectionListener(final ListSelectionListener listener) {
    list.removeListSelectionListener(listener);
  }

  @Override
  public synchronized void addMouseListener(final MouseListener listener) {
    list.addMouseListener(listener);
  }

  @Override
  public synchronized void removeMouseListener(final MouseListener listener) {
    list.removeMouseListener(listener);
  }

  @Override
  public synchronized void addMouseMotionListener(final MouseMotionListener listener) {
    list.addMouseMotionListener(listener);
  }

  @Override
  public synchronized void removeMouseMotionListener(final MouseMotionListener listener) {
    list.removeMouseMotionListener(listener);
  }

  @Override
  public synchronized void addMouseWheelListener(final MouseWheelListener listener) {
    list.addMouseWheelListener(listener);
  }

  @Override
  public synchronized void removeMouseWheelListener(final MouseWheelListener listener) {
    list.removeMouseWheelListener(listener);
  }

  //
  // --- private methods
  //

  private void setIconStyle(final int iconSize, final boolean iconShadow) {

    synchronized (ImageFileList.this) {

      this.iconSize = iconSize;
      this.iconShadow = iconShadow;

      icons.clear();

      renderer.fireIconStyleChanged();
      model.fireContentsChanged();

      icons.addToLoadingQueue(getImages());
    }
  }

}
