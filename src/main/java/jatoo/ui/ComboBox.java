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
