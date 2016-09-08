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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Improved {@link JFileChooser} with many new things like reopen into the last
 * used folder, image preview, folder preview and preview to the content of the
 * selected file.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 2.0, July 17, 2014
 */
@SuppressWarnings("serial")
public class FileChooser extends JFileChooser {

  /** The logger. */
  private final Log logger = LogFactory.getLog(getClass());

  /** The store folder. */
  private static final File STORE_FOLDER = new File(new File(new File(System.getProperty("user.home")), ".jatoo"), "ui");

  /** The default name of the store file. */
  private static final String STORE_FILE_DEFAULT_NAME = "file-chooser";

  /** The keys for the stored state properties. */
  private static final String STORE_FILE_DIRECTORY = "directory";

  /** The file where the current state of the file chooser is stored. */
  private final File storeFile;

  /**
   * Constructs a {@link FileChooser} with the specified store file id (used to
   * save the state in a different file). The file chooser will try to restore
   * last saved state.
   * 
   * @param storeFileId
   *          an unique id to be appended at the end of the default store file
   *          name (if <code>null</code> the default store file will be used)
   */
  public FileChooser(final String storeFileId) {

    if (storeFileId == null) {
      storeFile = new File(STORE_FOLDER, STORE_FILE_DEFAULT_NAME + ".properties");
    } else {
      storeFile = new File(STORE_FOLDER, STORE_FILE_DEFAULT_NAME + "-" + storeFileId + ".properties");
    }

    storeFile.getParentFile().mkdirs();

    addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(e.getPropertyName())) {
          storeCurrentState();
        }
      }
    });

    loadCurrentState();
  }

  /**
   * Constructs a {@link FileChooser} using the default store file.
   */
  public FileChooser() {
    this(null);
  }

  /**
   * Stores the current state.
   */
  public void storeCurrentState() {
    new Thread() {
      public void run() {
        synchronized (FileChooser.this) {

          FileOutputStream stream = null;

          try {

            stream = new FileOutputStream(storeFile);

            Properties p = new Properties();
            p.setProperty(STORE_FILE_DIRECTORY, getCurrentDirectory().getAbsolutePath());
            p.storeToXML(stream, null);
          }

          catch (IOException e) {
            logger.warn("Failed to save the current state.", e);
          }

          finally {
            if (stream != null) {
              try {
                stream.close();
              } catch (IOException e) {
                logger.error("Error closing the stream after saving the current state.", e);
              }
            }
          }
        }
      }
    }.start();
  }

  /**
   * Restores the last saved state.
   */
  public void loadCurrentState() {
    synchronized (FileChooser.this) {

      if (storeFile.exists()) {

        FileInputStream stream = null;

        try {

          stream = new FileInputStream(storeFile);

          Properties p = new Properties();
          p.loadFromXML(stream);

          setCurrentDirectory(new File(p.getProperty(STORE_FILE_DIRECTORY)));
        }

        catch (IOException e) {
          logger.error("Failed to load the current state.", e);
        }

        finally {
          if (stream != null) {
            try {
              stream.close();
            } catch (IOException e) {
              logger.error("Error closing the stream after loading the current state.", e);
            }
          }
        }
      }
    }
  }

}
