package jatoo.imager;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import jatoo.image.ImageMemoryCache;
import jatoo.image.ImageThumbnails;
import jatoo.image.ImageUtils;
import jatoo.ui.ImageLoader;
import jatoo.ui.UIUtils;

@SuppressWarnings("serial")
public class JaTooImager extends JFrame {

  static {
    UIUtils.setSystemLookAndFeel();
    // UITheme2.setTheme(JaTooImager.class);
  }

  public static void main(String[] args) throws Exception {
    // new JaTooImagerViewer(new File("src\\test\\resources\\image.jpg"));
    new JaTooImager(new File("d:\\OneDrive - PSS Prosoft Solutions\\Cristi - Personal\\Photos\\Share\\2013.10.21 - USA\\FB\\IMG_20131024_085105.jpg"));

    // System.out.println(Toolkit.getDefaultToolkit().getScreenSize());

    // System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth());
    // System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[1].getDisplayMode().getWidth());

    // BufferedImage image = ImageUtils.read("d:\\IMG_1670.jpg");
    // image = ImageUtils.resizeToFit(image, 1920);
    // ImageUtils.writeJPEG(image, new File("d:\\IMG_1670_2.jpg"), 5);
  }

  private ImageMemoryCache imageMemoryCache = new ImageMemoryCache();

  private ImageThumbnails thumbnails = new ImageThumbnails();

  private JaTooImagerCanvas canvas = new JaTooImagerCanvas();

  private List<File> files;
  private int filesIndex;

  private ImageLoader imageLoader;

  public JaTooImager(File file) throws Exception {

    files = new ArrayList<>(Arrays.asList(file.getParentFile().listFiles(new FileFilter() {
      public boolean accept(File file) {
        return file.isFile();
      }
    })));
    filesIndex = files.indexOf(file);

    setContentPane(canvas);

    UIUtils.forwardDragAsMove(canvas, this);
    // UIUtils.disableDecorations(JaTooImager.this);

    UIUtils.setActionForEscapeKeyStroke(canvas, new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        JaTooImager.this.setVisible(false);
        JaTooImager.this.dispose();
      }
    });

    UIUtils.setActionForLeftKeyStroke(canvas, new AbstractAction() {
      public void actionPerformed(ActionEvent e) {

        filesIndex--;
        if (filesIndex < 0) {
          filesIndex = files.size() - 1;
        }

        showImage(files.get(filesIndex));
      }
    });

    UIUtils.setActionForRightKeyStroke(canvas, new AbstractAction() {
      public void actionPerformed(ActionEvent e) {

        synchronized (files) {

          filesIndex++;
          if (filesIndex >= files.size()) {
            filesIndex = 0;
          }

          showImage(files.get(filesIndex));
        }
      }
    });

    setTitle(file.getName());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    UIUtils.centerWindowOnScreen(this, 0, 25, 25);
    setVisible(true);

    canvas.addMouseWheelListener(new MouseWheelListener() {
      public void mouseWheelMoved(MouseWheelEvent e) {

        Rectangle bounds = getBounds();

        if (e.getPreciseWheelRotation() > 0) {

          // setLocation(bounds.x - 5, bounds.y - 5);
          // setSize(bounds.width + 10, bounds.height + 10);

          // bounds.x += 15;
          // bounds.y += 15;
          bounds.width -= 30;
          bounds.height -= 30;
        }

        else {

          // setLocation(bounds.x - 5, bounds.y - 5);
          // setSize(bounds.width + 10, bounds.height + 10);

          // bounds.x -= 15;
          // bounds.y -= 15;
          bounds.width += 30;
          bounds.height += 30;

        }

        // setLocation(bounds.x, bounds.y);
        // setSize(bounds.width, bounds.height);
        setBounds(bounds);
      }
    });
  }

  private void showImage(final File file) {

    //
    // start the image loader
    // but only after the previous one is stopped

    BufferedImage imageCached = imageMemoryCache.get(file);
    if (imageCached != null) {
      showImage(file, imageCached);
      return;
    }

    canvas.showLoader();

    if (imageLoader != null) {
      imageLoader.forceStop();
    }

    imageLoader = new ImageLoader(file) {

      @Override
      protected void onLoaderStart(File file, boolean isForceStop) {

        BufferedImage thumbnail = thumbnails.get(file, 32, 32, false, true, ImageUtils.FORMAT.JPG);

        if (thumbnail != null) {
          showImage(file, thumbnail);
        } else {
          showImage(file, null);
        }
      }

      @Override
      protected void onImageLoaded(File file, BufferedImage image) {

        showImage(file, image);

        //
        // update the cache

        // imageCacheMemory.put(file, image);

        //
        //

        thumbnails.get(file, image, 32, 32, true, true, ImageUtils.FORMAT.JPG);
      }

      @Override
      protected void onLoaderStop(File file, boolean isForceStop) {
        if (!isForceStop) {
          canvas.hideLoader();
        }
      }
    };
  }

  private void showImage(final File file, final BufferedImage image) {

    canvas.setImage(image);

    if (image == null) {
      setIconImage(null);
    }

    else {
      setIconImage(thumbnails.get(file, 32, 32, true, false, ImageUtils.FORMAT.JPG));
    }
  }

}
