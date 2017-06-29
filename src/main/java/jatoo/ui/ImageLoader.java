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
