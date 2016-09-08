/*
 * Copyright (C) Cristian Sulea ( http://cristian.sulea.net )
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
