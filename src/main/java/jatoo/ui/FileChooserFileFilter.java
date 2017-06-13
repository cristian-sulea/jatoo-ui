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

import javax.swing.filechooser.FileFilter;

/**
 * A customizable filter for file choosers (filtering the set of files shown to
 * the user).
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 2.0, July 17, 2014
 */
public class FileChooserFileFilter extends FileFilter {

  /** The description of this file filter. */
  private final String description;

  /** The extensions to be filtered by this file filter. */
  private final String[] extensions;

  /**
   * Constructs a {@code FileChooserFileFilter} with the specified name and file
   * name extensions.
   * 
   * @param name
   *          the name of the filter
   * @param extensions
   *          the extensions to be filtered
   */
  public FileChooserFileFilter(final String name, final String... extensions) {

    if (extensions.length == 0) {
      throw new IllegalArgumentException("Useless to cerate a file filter with no extentions.");
    }

    this.description = createDescription(name, extensions);

    this.extensions = new String[extensions.length];

    for (int i = 0; i < extensions.length; i++) {
      this.extensions[i] = extensions[i].toLowerCase();
    }
  }

  @Override
  public boolean accept(File file) {

    boolean accept = false;

    if (file.isDirectory()) {
      accept = true;
    }

    else {

      final String fileName = file.getName().toLowerCase();

      for (String extension : extensions) {

        if (fileName.endsWith(extension)) {

          accept = true;
          break;
        }
      }
    }

    return accept;
  }

  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Creates a description for this filter based on the name and extensions. For
   * example: "Images (*.bmp, *.jpeg, *.png)".
   * 
   * @param name
   * @param extensions
   * @return
   */
  private String createDescription(final String name, final String... extensions) {

    final StringBuilder description = new StringBuilder();

    description.append(name);
    description.append(" (");

    for (int i = 0; i < extensions.length; i++) {
      description.append("*.").append(extensions[i]);
      if (i != extensions.length - 1) {
        description.append(", ");
      }
    }

    description.append(')');

    return description.toString();
  }

}
