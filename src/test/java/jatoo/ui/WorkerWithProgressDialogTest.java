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
