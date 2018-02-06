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

/**
 * A "loader" thread where images can be loaded one at the time, with the option to drop current loading.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 2.0, February 6, 2018
 */
public class ImageLoaderV2 implements Runnable {

  private final Log logger = LogFactory.getLog(getClass());

  private ImageLoaderV2Listener[] listeners;

  private final Thread thread;
  private boolean threadStopped;

  private File file;
  private FileInputStream stream;

  private boolean loadingStarted;
  private boolean loadingStopped;

  public ImageLoaderV2(final ImageLoaderV2Listener... listeners) {

    this.listeners = listeners;

    thread = new Thread(this);
    thread.setDaemon(true);
  }

  public void stopThread() {

    threadStopped = true;

    synchronized (this) {
      notifyAll();
    }
  }

  public void startLoading(File file) {

    if (!thread.isAlive()) {
      synchronized (thread) {
        if (!thread.isAlive()) {
          threadStopped = false;
          thread.start();
        }
      }
    }

    stopLoading();

    this.file = file;

    synchronized (this) {
      loadingStarted = true;
      notifyAll();
    }
  }

  public void startReloading() {
    startLoading(file);
  }

  public void stopLoading() {

    loadingStopped = true;

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

    while (!threadStopped) {

      for (ImageLoaderV2Listener listener : listeners) {
        listener.onStartLoading(file);
      }

      loadingStarted = false;
      loadingStopped = false;

      BufferedImage image;

      try {

        stream = new FileInputStream(file);
        image = ImageUtils.read(stream);

        for (ImageLoaderV2Listener listener : listeners) {
          listener.onImageLoaded(file, image);
        }
      }

      catch (IOException e) {

        image = null;

        if (!loadingStopped) {

          for (ImageLoaderV2Listener listener : listeners) {
            listener.onImageError(file, e);
          }

          logger.warn("image could not be read from file: " + file, e);
        }
      }

      finally {
        if (stream != null) {
          try {
            stream.close();
          } catch (IOException e) {
            logger.warn("stream could not be closed: " + file, e);
          }
        }
      }

      synchronized (this) {

        if (loadingStarted) {
          continue;
        }

        try {
          wait();
        } catch (InterruptedException e) {
          logger.error("thread can't wait ???...", e);
        }
      }
    }
  }
}
