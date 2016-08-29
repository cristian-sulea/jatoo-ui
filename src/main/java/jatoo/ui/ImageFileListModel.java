/*
 * Copyright (C) 2014 Cristian Sulea ( http://cristian.sulea.net )
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatoo.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

/**
 * The model for {@link ImageFileList} component.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.0, July 16, 2014
 */
@SuppressWarnings("serial")
public class ImageFileListModel extends DefaultListModel<File> {

  public ImageFileListModel() {}

  public void addImage(final File file) {
    addElement(file);
  }

  public void addImages(final List<File> files) {
    for (File file : files) {
      addElement(file);
    }
  }

  public List<File> getImages() {

    final List<File> files = new ArrayList<>(size());

    for (int i = 0, n = size(); i < n; i++) {
      files.add(get(i));
    }

    return files;
  }

  public void removeImage(final File file) {
    removeElement(file);
  }

  public void removeImages(final List<File> files) {
    for (File file : files) {
      removeElement(file);
    }
  }

  public void removeAllImages() {
    removeAllElements();
  }

  public void fireContentsChanged() {
    super.fireContentsChanged(this, 0, size());
  }

  public void fireContentsChanged(int index0, int index1) {
    super.fireContentsChanged(this, index0, index1);
  }

}
