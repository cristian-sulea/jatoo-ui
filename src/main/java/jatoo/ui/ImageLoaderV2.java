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

public abstract class ImageLoaderV2 implements Runnable {

  private final Log logger = LogFactory.getLog(getClass());

  private File file;
  private FileInputStream stream;

  private boolean started;
  private boolean stopped;

  private boolean firstRun = true;

  public ImageLoaderV2() {
    new Thread(this).start();
  }

  public void startLoading(File file) {

    stopLoading();

    this.file = file;

    synchronized (this) {
      this.started = true;
      this.notifyAll();
    }
  }

  public void startReloading() {
    startLoading(this.file);
  }

  public void stopLoading() {

    this.stopped = true;

    if (stream != null) {
      try {
        stream.close();
      } catch (IOException e) {
        logger.warn("image stream could not be closed: " + file, e);
      }
    }
  }

  @Override
  public void run() {

    while (true) {

      if (firstRun) {
        firstRun = false;
        synchronized (this) {
          try {
            this.wait();
          } catch (InterruptedException e) {
            logger.error("thread can't wait ???...", e);
          }
        }
      }

      onStartLoading(file);

      this.started = false;
      this.stopped = false;

      BufferedImage image;

      try {
        stream = new FileInputStream(file);
        image = ImageUtils.read(stream);
      }

      catch (IOException e) {

        image = null;

        if (!stopped) {
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

      onImageLoaded(file, image);

      synchronized (this) {

        if (started) {
          continue;
        }

        try {
          this.wait();
        } catch (InterruptedException e) {
          logger.error("thread can't wait ???...", e);
        }
      }
    }
  }

  protected abstract void onStartLoading(File file);

  protected abstract void onImageLoaded(File file, BufferedImage image);

}
