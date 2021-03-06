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

import javax.swing.JDialog;

/**
 * A specialized {@link JDialog} implementation for the {@link AppWindow} base class.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.2, February 8, 2018
 */
public class AppWindowDialog extends AppWindow {

  public AppWindowDialog(final File propertiesFolder) {
    super(new JDialog(), propertiesFolder);
  }

  /**
   * @see getDialog#setDefaultCloseOperation(int)
   */
  @Override
  public void setDefaultCloseOperation(int operation) {
    getDialog().setDefaultCloseOperation(operation);
  }

  @Override
  public void setTitle(String title) {
    getDialog().setTitle(title);
  }

  private JDialog getDialog() {
    return (JDialog) getWindow();
  }
}
