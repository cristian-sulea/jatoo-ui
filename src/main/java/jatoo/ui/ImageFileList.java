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

import jatoo.image.ImageFileFilter;
import jatoo.image.ImageUtils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

/**
 * A component that displays a list of images from {@link File}s.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.5, September 8, 2014
 */
@SuppressWarnings("serial")
public class ImageFileList extends JPanel {

  private final JList<File> list;
  private final ImageFileListModel model;
  private final ImageFileListCellRenderer renderer;

  private final JScrollPane listScrollPane;

  private final Button addButton;
  private final Button removeSelectedButton;
  private final Button removeAllButton;

  public ImageFileList() {
    this(2, 3);
  }

  public ImageFileList(int rows, int columns) {

    //
    // model and cell renderer

    list = new JList<>();
    list.setModel(model = new ImageFileListModel());
    list.setCellRenderer(renderer = new ImageFileListCellRenderer(100, true));

    //
    // dummy files for the initial minimum size
    // do not set preferred size, scroll panes go crazy
    // scrollPane.getViewport().setPreferredSize(list.getMinimumSize());

    list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    list.setVisibleRowCount(rows);

    for (int i = 1; i <= (rows * columns - 1); i++) {
      model.addImage(new File(""));
    }

    list.setMinimumSize(list.getPreferredSize());

    model.removeAllImages();

    list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    list.setVisibleRowCount(-1);

    //
    // remove actions on arrows (by setting a random action map key)
    // better to not work at all, rather than work in a wrong way

    // final Object actionMapKey = getClass() +
    // String.valueOf(System.currentTimeMillis());
    //
    // list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
    // actionMapKey);
    // list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
    // actionMapKey);
    // list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
    // actionMapKey);
    // list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
    // actionMapKey);

    //
    // list container

    listScrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    listScrollPane.getViewport().setPreferredSize(list.getMinimumSize());

    //
    // buttons

    // UITheme.getIcon(ImageFileList.class, "add-images");

    addButton = new Button(UITheme.getText(ImageFileList.class, "AddImages"), UITheme.getIcon(ImageFileList.class, "AddImages"));
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        final FileChooser fc = new FileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileChooserFileFilter("Images", ImageUtils.getFormatNames()));

        if (fc.showOpenDialog(ImageFileList.this) == JFileChooser.APPROVE_OPTION) {
          addImages(Arrays.asList(fc.getSelectedFiles()));
        }
      }
    });

    removeSelectedButton = new Button(UITheme.getText(ImageFileList.class, "RemoveSelected"), UITheme.getIcon(ImageFileList.class, "RemoveSelected"));
    removeSelectedButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        removeSelectedImages();
      }
    });

    removeAllButton = new Button(UITheme.getText(ImageFileList.class, "RemoveAll"), UITheme.getIcon(ImageFileList.class, "RemoveAll"));
    removeAllButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        removeAllImages();
      }
    });

    //
    // layout

    setLayout(new MigLayout("insets 0", "[fill][fill]push[fill]", "[fill, grow][fill]"));
    add(listScrollPane, "spanx");
    add(addButton);
    add(removeSelectedButton);
    add(removeAllButton);

    //
    // set the minimum size
    // actually the height (to not be smaller than the specified rows)

    setMinimumSize(new Dimension(0, getPreferredSize().height));
  }

  public void addImage(final File file) {

    if (ImageFileFilter.getInstance().accept(file)) {

      model.addImage(file);
      renderer.refreshImage(this, model, file);
    }
  }

  public void addImages(final List<File> files) {

    List<File> imageFiles = new ArrayList<>(files.size());

    for (File file : files) {
      if (ImageFileFilter.getInstance().accept(file)) {
        imageFiles.add(file);
      }
    }

    model.addImages(imageFiles);
    renderer.refreshImages(this, model, imageFiles);
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
    model.removeImages(getSelectedImages());
  }

  public void removeAllImages() {
    model.removeAllImages();
  }

  public int getImagesCount() {
    return model.size();
  }

  public void refreshImage(final File file) {
    renderer.refreshImage(this, model, file);
  }

  public void refreshImages(final List<File> files) {
    renderer.refreshImages(this, model, files);
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

}
