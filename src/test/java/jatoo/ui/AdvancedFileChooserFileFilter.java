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
