/*
 * Copyright (C) Cristian Sulea ( http://cristiansulea.entrust.ro )
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package jatoo.ui;

import java.io.File;
import java.util.Locale;

import javax.swing.filechooser.FileFilter;

/**
 * Helper class to ease the add of file filters in the advanced file chooser.
 * 
 * @version 1.0.4, June 17, 2011
 * @author Cristian Sulea ( http://cristiansulea.entrust.ro )
 */
public class AdvancedFileChooserFileFilter extends FileFilter {

  /**
   * The name for this file filter.
   */
  private String name;

  /**
   * The extentions to be filtered by this file filter.
   */
  private String[] extensions;

  /**
   * The constructor for the {@link AdvancedFileChooserFileFilter} object.
   * 
   * @param name
   *          the name of the filter.
   * @param extensions
   *          the extensions to be filtered.
   */
  public AdvancedFileChooserFileFilter(String name, String... extensions) {

    if (extensions.length == 0) {
      throw new IllegalArgumentException("Useless to declare a file filter with no extentions.");
    }

    this.name = name;
    this.extensions = extensions;

    for (int i = 0; i < extensions.length; i++) {
      this.extensions[i] = this.extensions[i].toLowerCase(Locale.getDefault());
    }
  }

  /**
   * Checks if the given file is accepted by this filter.
   * 
   * @param file
   *          File
   * 
   * @return boolean <code>true</code> if the file is accepted;
   *         <code>false</code> otherwise.
   */
  @Override
  public boolean accept(File file) {

    if (file.isDirectory()) {
      return true;
    }

    String fileName = file.getName().toLowerCase(Locale.getDefault());

    for (int i = 0; i < extensions.length; i++) {
      if (fileName.endsWith(extensions[i])) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the description of this filter. For example: "JPG and GIF Images".
   * 
   * @return String the description of this filter.
   */
  @Override
  public String getDescription() {

    StringBuilder sb = new StringBuilder();

    sb.append(name).append(" (");

    for (int i = 0; i < extensions.length; i++) {

      sb.append("*.").append(extensions[i]);

      if (i != extensions.length - 1) {
        sb.append(", ");
      }
    }

    sb.append(')');

    return sb.toString();
  }

}
