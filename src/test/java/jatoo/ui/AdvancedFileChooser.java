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

import java.awt.Component;
import java.awt.HeadlessException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

/**
 * An advanced file chooser with many new things like reopen into the last used
 * folder, image preview, folder preview and preview to the content of the
 * selected file.
 * 
 * @version 1.1.0, September 23, 2011
 * @author Cristian Sulea ( http://cristiansulea.entrust.ro )
 */
public class AdvancedFileChooser extends JFileChooser {

  /** serialVersionUID */
  private static final long serialVersionUID = 1L;

  /** the logger */
  private static final Logger LOGGER = Logger.getLogger(AdvancedFileChooser.class.getName());

  /**
   * The file where the current state of the file chooser is stored. Is also
   * used as LOCK for various operations with the file containing the current
   * directory.
   */
  private File currentStateStorageFile;

  /**
   * The accessory component for the advanced file chooser.
   */
  private AdvancedFileChooserAccessory accessory;

  private String dialogTitle;

  /**
   * The main constructor of the {@link AdvancedFileChooser} component. If is
   * specified (is not <code>null</code>), the specified id will be used to save
   * in a different file the state of this file chooser. The file chooser will
   * try to restore his state, based on the specified id.
   * 
   * @param uniqueId
   *          an unique id to save and restore the state.
   */
  public AdvancedFileChooser(String uniqueId) {
    super();

    currentStateStorageFile = new File(System.getProperty("user.home"));

    if (uniqueId == null) {
      currentStateStorageFile = new File(currentStateStorageFile, "jaco-swing-advanced-file-chooser.txt");
    } else {
      currentStateStorageFile = new File(currentStateStorageFile, uniqueId + ".txt");
    }

    setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

    setAccessory(accessory = new AdvancedFileChooserAccessory(this));

    addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(e.getPropertyName())) {
          saveCurrentState();
        }
      }
    });

    loadCurrentState();
  }

  /**
   * Default constructor for the {@link AdvancedFileChooser} component.
   * 
   * @see AdvancedFileChooser#AdvancedFileChooser(String)
   */
  public AdvancedFileChooser() {
    this(null);
  }

  /**
   * Sets the title of the Dialog.
   */
  public void setDialogTitle(String title) {
    this.dialogTitle = title;
  }

  /**
   * Stores the current state in a file based on the unique id.
   */
  public void saveCurrentState() {
    new Thread() {
      public void run() {
        synchronized (currentStateStorageFile) {

          BufferedWriter writer = null;

          try {
            writer = new BufferedWriter(new FileWriter(currentStateStorageFile));
            writer.write(getCurrentDirectory().getAbsolutePath());
            writer.newLine();
            writer.write(Boolean.toString(isShowPreview()));
          }

          catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to save the current directory.", e);
          }

          finally {
            if (writer != null) {
              try {
                writer.close();
              } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error closing the writer to current directory storage file.", e);
              }
            }
          }
        }
      }
    }.start();
  }

  /**
   * Restores the state of the file chooser from a file based on the unique id.
   */
  public void loadCurrentState() {
    synchronized (currentStateStorageFile) {

      BufferedReader reader = null;

      try {
        reader = new BufferedReader(new FileReader(currentStateStorageFile));
        setCurrentDirectory(new File(reader.readLine()));
        setShowPreview(Boolean.parseBoolean(reader.readLine()));
      }

      catch (IOException e) {
        LOGGER.log(Level.FINE, "Failed to load the current directory.", e);
      }

      finally {
        if (reader != null) {
          try {
            reader.close();
          } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error closing the reader to current directory storage file.", e);
          }
        }
      }
    }
  }

  /**
   * Returns the state for the show preview.
   * 
   * @return the state for the show preview.
   */
  public boolean isShowPreview() {
    return accessory.isShowPreview();
  }

  /**
   * Enable or disable the show preview.
   * 
   * @param showPreview
   *          the new state for the show preview.
   */
  public void setShowPreview(boolean showPreview) {
    accessory.setShowPreview(showPreview);
  }

  @Override
  protected JDialog createDialog(Component parent) throws HeadlessException {

    if (dialogTitle == null) {
      return super.createDialog(parent);
    }

    else {
      JDialog dialog = super.createDialog(parent);
      dialog.setTitle(dialogTitle);
      return dialog;
    }
  }

}
