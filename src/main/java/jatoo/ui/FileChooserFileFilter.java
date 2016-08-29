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
