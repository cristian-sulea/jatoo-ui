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

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.logging.LogFactory;

@SuppressWarnings("serial")
public class TestFrame extends JFrame {

  static {

    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
    System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "trace");

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
      LogFactory.getLog(TestFrame.class).error("LookAndFeel Error", e);
    }
  }

  public TestFrame(Class<? extends Container> container) {
    super(container.getSimpleName());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    try {
      setContentPane(container.newInstance());
      pack();
    } catch (InstantiationException | IllegalAccessException e) {
      LogFactory.getLog(TestFrame.class).error("container.newInstance()", e);
      setSize(400, 300);
    }

    setLocationRelativeTo(null);
    setVisible(true);
  }

}
