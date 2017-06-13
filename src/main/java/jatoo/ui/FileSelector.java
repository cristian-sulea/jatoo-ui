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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

/**
 * A file selector component (similar to the one from HTML).
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 2.1, August 13, 2014
 */
@SuppressWarnings("serial")
public class FileSelector extends JComponent {

  /** The text field that will keep the absolute path of the selected file. */
  private TextField fileTextField;

  /** The button that will launch the file chooser. */
  private Button fileChooserButton;

  /** The selected file. */
  private File file;

  /**
   * Constructs a {@link FileSelector} with the specified select button text and
   * file chooser approve button text.
   * 
   * @param fileChooserButtonText
   *          the text for the select button (that launch the file chooser)
   * @param approveButtonText
   *          the text for the approve button (on the file chooser)
   */
  public FileSelector(final String fileChooserButtonText, final String approveButtonText) {

    //
    // the text field

    fileTextField = new TextField();
    fileTextField.setEditable(false);

    //
    // the button

    fileChooserButton = new Button(fileChooserButtonText);
    fileChooserButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        final FileChooser fc = new FileChooser();
        fc.setFileSelectionMode(selectionMode);
        fc.setSelectedFile(file);

        if (FileChooser.APPROVE_OPTION == fc.showDialog(SwingUtilities.getWindowAncestor(FileSelector.this), approveButtonText)) {
          setFile(fc.getSelectedFile());
        }
      }
    });

    //
    // layout the component

    setLayout(new MigLayout("insets 0, fill", "[fill, grow][fill]", "[fill, grow]"));
    add(fileTextField);
    add(fileChooserButton);
  }

  /**
   * Constructs a {@link FileSelector}.
   */
  public FileSelector() {
    this(UITheme.getText(FileSelector.class, "FileChooserButtonText"), UITheme.getText(FileSelector.class, "FileChooser.ApproveButtonText"));
  }

  public void doSelectFile() {
    fileChooserButton.doClick();
  }

  private int selectionMode = JFileChooser.FILES_ONLY;

  public final void setSelectionModeFilesOnly() {
    selectionMode = JFileChooser.FILES_ONLY;
  }

  public final void setSelectionModeFoldersOnly() {
    selectionMode = JFileChooser.DIRECTORIES_ONLY;
  }

  public final void setSelectionModeFilesAndFolders() {
    selectionMode = JFileChooser.FILES_AND_DIRECTORIES;
  }

  /**
   * Returns the selected file.
   * 
   * @return the selected file.
   */
  public final File getFile() {
    return file;
  }

  /**
   * Programmatically sets the selected file.
   * 
   * @param file
   *          the new value (as {@link File}) of the selected file.
   */
  public final void setFile(final File file) {

    this.file = file;

    if (file == null) {
      fileTextField.setText("");
    }

    else {
      fileTextField.setText(file.getAbsolutePath());
      fileTextField.setCaretPosition(fileTextField.getText().length());
    }
  }

  /**
   * Programmatically sets the selected file.
   * 
   * @param file
   *          the new value (as {@link String}) of the selected file.
   */
  public final void setFile(final String file) {

    if (file == null) {
      this.file = null;
      this.fileTextField.setText("");
    }

    else {
      this.file = new File(file);
      this.fileTextField.setText(this.file.getAbsolutePath());
      this.fileTextField.setCaretPosition(fileTextField.getText().length());
    }
  }

}
