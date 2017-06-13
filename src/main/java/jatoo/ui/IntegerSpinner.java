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

import java.awt.Color;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * A single line input field that lets the user select an integer value from an
 * ordered sequence.
 * <p>
 * Spinners typically provide a pair of tiny arrow buttons for stepping through
 * the elements of the sequence. The keyboard up/down arrow keys also cycle
 * through the elements.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 1.1, September 3, 2014
 */
@SuppressWarnings("serial")
public class IntegerSpinner extends JSpinner {

  private final JSpinner.NumberEditor editor;

  public IntegerSpinner(int value, int minimum, int maximum, int stepSize, String pattern) {
    super(new SpinnerNumberModel(value, minimum, maximum, stepSize));

    if (pattern == null) {
      editor = new JSpinner.NumberEditor(this);
    } else {
      editor = new JSpinner.NumberEditor(this, pattern);
    }

    final Color editorBackground = editor.getTextField().getBackground();
    editor.getTextField().setEditable(false);
    editor.getTextField().setBackground(editorBackground);

    this.setEditor(editor);
  }

  public IntegerSpinner(int value, int minimum, int maximum, int stepSize) {
    this(value, minimum, maximum, stepSize, null);
  }

  @Override
  public Integer getValue() {
    return (Integer) super.getValue();
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);

    if (enabled) {

      JSpinner.NumberEditor editor = (JSpinner.NumberEditor) getEditor();
      editor.getTextField().setEditable(true);
      Color editorBackground = editor.getTextField().getBackground();
      editor.getTextField().setEditable(false);
      editor.getTextField().setBackground(editorBackground);
    }
  }

  /**
   * Sets the number of columns in this spinner, and then invalidate the layout.
   * 
   * @param columns
   *          the number of columns >= 0
   * @exception IllegalArgumentException
   *              if <code>columns</code> is less than 0
   */
  public void setColumns(int columns) {
    editor.getTextField().setColumns(4);
  }

}
