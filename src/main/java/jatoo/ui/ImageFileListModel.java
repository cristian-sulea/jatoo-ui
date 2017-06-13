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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

/**
 * The model for {@link ImageFileList} component.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 2.0-SNAPSHOT, June 9, 2017
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
