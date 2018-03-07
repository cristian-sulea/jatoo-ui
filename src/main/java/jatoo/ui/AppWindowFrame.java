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

import javax.swing.JFrame;

/**
 * A specialized {@link JFrame} implementation for the {@link AppWindow} base class.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.1, February 8, 2018
 */
public class AppWindowFrame extends AppWindow {

  public AppWindowFrame(final File propertiesFolder) {
    super(new JFrame(), propertiesFolder);
  }

  /**
   * @see JFrame#setDefaultCloseOperation(int)
   */
  @Override
  public void setDefaultCloseOperation(int operation) {
    getFrame().setDefaultCloseOperation(operation);
  }

  @Override
  public void setTitle(String title) {
    getFrame().setTitle(title);
  }

  private JFrame getFrame() {
    return (JFrame) getWindow();
  }
}
