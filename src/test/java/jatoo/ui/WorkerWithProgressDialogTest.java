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

import javax.swing.JFrame;
import javax.swing.JPanel;

public class WorkerWithProgressDialogTest {

  public static void main(String[] args) throws Exception {

    JPanel cp = new JPanel();

    final JFrame frame = new JFrame(WorkerWithProgressDialogTest.class.getSimpleName());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(cp);
    frame.setSize(100, 100);
    frame.setLocationRelativeTo(null);
    frame.setLocation(600, 500);
    frame.setVisible(true);

    new WorkerWithProgressDialog(cp, "xxx") {

      @Override
      protected void work() {

        for (int i = 0; i < 100; i++) {

          if (isCancelled()) {
            break;
          }

          try {
            Thread.sleep(100);
          } catch (Exception e) {}

          setProgress(100 * (i + 1) / 100);
          setMessage(i + " xxxxx");
        }
      }

      @Override
      protected void done() {
        System.out.println("done");
      }
    }.start();
  }

}
