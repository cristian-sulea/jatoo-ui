/*
 * Copyright (C) Cristian Sulea ( http://cristiansulea.entrust.ro )
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package jatoo.ui;

import jatoo.image.ImageUtils;
//import jatoo.image.ImageUtilsOldMethods;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * The accessory class for the advanced file chooser.
 * 
 * @version 1.0.3, June 17, 2011
 * @author Cristian Sulea ( http://cristiansulea.entrust.ro )
 */
public class AdvancedFileChooserAccessory extends JPanel {

  /** serialVersionUID */
  private static final long serialVersionUID = 1L;

  /**
   * the logger
   */
  private static final Logger LOGGER = Logger.getLogger(AdvancedFileChooserAccessory.class.getName());

  /**
   * The rapport between default font size of a text area and font size for the
   * preview.
   */
  private static final float PREVIEW_FONT_SIZE_SCALE = 2f / 3f;

  /**
   * Preferred size for the preview contained.
   */
  private static final Dimension PREVIEW_CONTAINER_PREFERRED_SIZE = new Dimension(150, 150);

  /**
   * The space from the left side of the accessory component. Will be set using
   * an empty border with the left size having this value.
   */
  private static final int SPACE = 5;

  /**
   * A check box used to enable/disable the preview.
   */
  private JCheckBox showPreviewCheckBox;

  /**
   * Holds the preview characters of the selected file.
   */
  private JTextArea previewTextArea;

  /**
   * Holds the preview of the selected image.
   */
  private JLabel previewLabel;

  /**
   * The contained for the preview components. Will have a card layout and will
   * simply show the needed component.
   */
  private JPanel previewContainer;

  /**
   * The last file showed on the preview component. When the user disable the
   * preview and then enable again, we have to show the preview of the last
   * selected file.
   */
  private File lastSelectedFile;

  /**
   * Constructs a new {@link AdvancedFileChooserAccessory} object.
   * 
   * @param fileChooser
   *          the instance for the {@link AdvancedFileChooser} object where this
   *          accessory will be linked.
   */
  public AdvancedFileChooserAccessory(final AdvancedFileChooser fileChooser) {

    fileChooser.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {

        String propertyName = e.getPropertyName();

        if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(propertyName) || JFileChooser.SELECTED_FILES_CHANGED_PROPERTY.equals(propertyName)) {

          if (fileChooser.isMultiSelectionEnabled()) {

            File[] selectedFiles = fileChooser.getSelectedFiles();

            if (selectedFiles.length == 1) {
              showPreview(selectedFiles[0]);
            } else {
              showPreview(null);
            }
          }

          else {
            showPreview(fileChooser.getSelectedFile());
          }
        }
      }
    });

    showPreviewCheckBox = new JCheckBox("Preview", false);
    showPreviewCheckBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        fileChooser.saveCurrentState();
        showPreview(lastSelectedFile);
      }
    });

    previewTextArea = new JTextArea();
    previewTextArea.setEditable(false);
    previewTextArea.setFont(previewTextArea.getFont().deriveFont(previewTextArea.getFont().getSize() * PREVIEW_FONT_SIZE_SCALE));

    previewLabel = new JLabel();
    previewLabel.setHorizontalAlignment(JLabel.CENTER);

    JPanel previewLabelContainer = new JPanel(new BorderLayout());
    previewLabelContainer.add(previewLabel, BorderLayout.NORTH);

    previewContainer = new JPanel(new CardLayout());
    previewContainer.add("TextArea", new JScrollPane(previewTextArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
    previewContainer.add("Label", new JScrollPane(previewLabelContainer, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

    previewContainer.setPreferredSize(PREVIEW_CONTAINER_PREFERRED_SIZE);

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(0, SPACE, 0, 0));
    add(showPreviewCheckBox, BorderLayout.NORTH);
    add(previewContainer, BorderLayout.CENTER);
  }

  /**
   * Returns the value of the show preview check box.
   * 
   * @return the value of the show preview check box.
   */
  public boolean isShowPreview() {
    return showPreviewCheckBox.isSelected();
  }

  /**
   * Sets the value of the show preview check box.
   * 
   * @param showPreview
   *          the new value for the show preview check box.
   */
  public void setShowPreview(boolean showPreview) {
    showPreviewCheckBox.setSelected(showPreview);
  }

  /**
   * This is the place where the preview is created and displayed.
   * 
   * @param file
   *          the file that will be previewed.
   */
  private void showPreview(File file) {

    lastSelectedFile = file;

    //
    // reset and return if no preview checked

    previewTextArea.setText("");
    previewLabel.setIcon(null);

    if (!showPreviewCheckBox.isSelected() || file == null) {
      return;
    }

    //
    // if is a file show the content

    if (file.isFile()) {

      String fileName = file.getName().toLowerCase(Locale.getDefault());

      if (fileName.endsWith(".gif") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
        try {
          BufferedImage image = ImageUtils.read(file);
          //TODO
          //image = ImageUtilsOldMethods.resize(image, previewContainer.getWidth(), previewContainer.getHeight());
          image = null;
          previewLabel.setIcon(new ImageIcon(image));
          ((CardLayout) previewContainer.getLayout()).show(previewContainer, "Label");
          return;
        } catch (IOException e) {
          LOGGER.log(Level.INFO, "preview image creation failed", e);
        }
      }

      BufferedReader reader = null;

      try {

        reader = new BufferedReader(new FileReader(file));

        int maxLines = previewTextArea.getHeight() / previewTextArea.getFont().getSize() + 1;

        String line;

        for (int i = 1; i <= maxLines; i++) {

          line = reader.readLine();

          if (line == null) {
            break;
          }

          previewTextArea.append(line);
          previewTextArea.append(System.getProperty("line.separator"));
        }
      }

      catch (IOException e) {
        LOGGER.log(Level.WARNING, "error reading the file: " + file, e);
      }

      finally {
        if (reader != null) {
          try {
            reader.close();
          } catch (Exception e) {
            LOGGER.log(Level.WARNING, "the reader failed to close", e);
          }
        }
      }
    }

    //
    // if is a directory try to list the files and directories

    else if (file.isDirectory()) {

      for (String fileName : file.list()) {
        previewTextArea.append(fileName);
        previewTextArea.append(System.getProperty("line.separator"));
      }
    }

    //
    // move the caret to the top
    // to be sure we have the preview starting from the first line

    previewTextArea.setCaretPosition(0);

    ((CardLayout) previewContainer.getLayout()).show(previewContainer, "TextArea");
  }
}
