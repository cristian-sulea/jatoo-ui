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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jatoo.image.ImageUtils;

public abstract class ImageLoader implements Runnable {

  private final Log logger = LogFactory.getLog(getClass());

  private final File file;

  private FileInputStream stream;

  private boolean isForceStop = false;

  public ImageLoader(final File file) {
    this(file, true);
  }

  public ImageLoader(final File file, final boolean start) {

    this.file = file;

    if (start) {
      start();
    }
  }

  public void start() {
    new Thread(this).start();
  }

  @Override
  public void run() {

    onLoaderStart(file, isForceStop);

    BufferedImage image;

    try {
      stream = new FileInputStream(file);
      image = ImageUtils.read(stream);
    }

    catch (IOException e) {

      image = null;

      if (!isForceStop) {
        logger.warn("image could not be read from file: " + file, e);
      }
    }

    finally {
      try {
        stream.close();
      } catch (IOException e) {
        logger.warn("stream could not be closed: " + file, e);
      }
    }

    if (!isForceStop) {
      onImageLoaded(file, image);
    }

    onLoaderStop(file, isForceStop);
  }

  public void forceStop() {

    isForceStop = true;

    if (stream != null) {
      try {
        stream.close();
      } catch (IOException e) {
        logger.warn("image stream could not be closed: " + file, e);
      }
    }
  }

  protected void onLoaderStart(final File file, final boolean isForceStop) {}

  protected abstract void onImageLoaded(final File file, final BufferedImage image);

  protected void onLoaderStop(final File file, final boolean isForceStop) {}

}
