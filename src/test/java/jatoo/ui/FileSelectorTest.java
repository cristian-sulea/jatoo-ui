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

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FileSelectorTest {

  public static void main(String[] args) throws Exception {

    JPanel cp = new JPanel(new GridLayout());
    cp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    cp.add(new FileSelector());

    final JFrame frame = new JFrame(FileSelectorTest.class.getSimpleName());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(cp);
    frame.pack();
    frame.setSize(500, frame.getHeight());
    frame.setLocationRelativeTo(null);
    frame.setLocation(600, 500);
    frame.setVisible(true);
  }

}
