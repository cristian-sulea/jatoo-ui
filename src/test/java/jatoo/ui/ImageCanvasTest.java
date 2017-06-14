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

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import jatoo.image.ImageUtils;

@SuppressWarnings("serial")
public class ImageCanvasTest extends JPanel {

  public static void main(String[] args) {

    final ImageCanvas imageCanvas = new ImageCanvas();

    new TestFrame(imageCanvas, 400, 400);

    for (File file : new File("src\\test\\resources\\jatoo\\ui\\").listFiles()) {
      if (file.isFile()) {

        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            try {
              imageCanvas.setImage(ImageUtils.read(file));
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });

        try {
          Thread.sleep(5000);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

}
