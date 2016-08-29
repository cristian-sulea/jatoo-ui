/*
 * Copyright (C) 2014 Cristian Sulea ( http://cristian.sulea.net )
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatoo.ui;

import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;

/**
 * A component that combines a button or editable field and a drop-down list.
 * The user can select a value from the drop-down list, which appears at the
 * user's request. If you make the combo box editable, then the combo box
 * includes an editable field into which the user can type a value.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 2.0, August 25, 2014
 */
@SuppressWarnings("serial")
public class ComboBox<E> extends JComboBox<E> {

  private TextField editorComponent;

  public ComboBox() {
    super();
    init();
  }

  public ComboBox(ComboBoxModel<E> aModel) {
    super(aModel);
    init();
  }

  public ComboBox(E[] items) {
    super(items);
    init();
  }

  public ComboBox(Vector<E> items) {
    super(items);
    init();
  }

  private void init() {

    editorComponent = new TextField();
    editorComponent.setBorder(((JComponent) getEditor().getEditorComponent()).getBorder());

    setEditor(new BasicComboBoxEditor() {
      protected JTextField createEditorComponent() {
        return editorComponent;
      }
    });
  }

  public TextField getEditorComponent() {
    return editorComponent;
  }

}
