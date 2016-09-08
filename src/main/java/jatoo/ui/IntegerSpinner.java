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
