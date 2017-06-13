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
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

/**
 * A slider component with a spinner attached.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 2.0, August 19, 2014
 */
@SuppressWarnings("serial")
public class SliderWithSpinner extends JComponent {

  /** The slider. */
  private final JSlider slider;

  /** The spinner. */
  private final JSpinner spinner;

  /**
   * Creates a horizontal slider using the specified min, max and value.
   * 
   * @param min
   *          the minimum value of the slider
   * @param max
   *          the maximum value of the slider
   * @param value
   *          the initial value of the slider
   */
  public SliderWithSpinner(int min, int max, int value) {

    //
    // the slider

    slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
    slider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        spinner.setValue(slider.getValue());
      }
    });

    slider.setPreferredSize(new Dimension(Math.min(max, 100), slider.getPreferredSize().height));

    //
    // the spinner

    spinner = new JSpinner();
    spinner.setModel(new SpinnerNumberModel(value, min, max, 1));
    spinner.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        slider.setValue((Integer) spinner.getValue());
      }
    });

    //
    // new editor for the spinner

    setSpinnerPattern(null);

    //
    // layout the component

    setLayout(new MigLayout("insets 0, fill", "[fill, grow][fill]", "[fill, grow]"));
    add(slider);
    add(spinner);
  }

  /**
   * Creates a horizontal slider using the specified min and max with an initial
   * value equal to the average of the min plus max.
   * 
   * @param min
   *          the minimum value of the slider
   * @param max
   *          the maximum value of the slider
   */
  public SliderWithSpinner(int min, int max) {
    this(min, max, (min + max) / 2);
  }

  /**
   * Creates a horizontal slider with the range 0 to 100 and an initial value of
   * 50.
   */
  public SliderWithSpinner() {
    this(0, 100, 50);
  }

  public void setSpinnerPattern(String pattern) {

    JSpinner.NumberEditor editor;

    if (pattern == null) {
      editor = new JSpinner.NumberEditor(spinner);
    } else {
      editor = new JSpinner.NumberEditor(spinner, pattern);
    }

    Color editorBackground = editor.getTextField().getBackground();
    editor.getTextField().setEditable(false);
    editor.getTextField().setBackground(editorBackground);

    spinner.setEditor(editor);
  }

  public void setValue(int n) {
    slider.setValue(n);
  }

  public int getValue() {
    return slider.getValue();
  }

  public void addChangeListener(ChangeListener l) {
    slider.addChangeListener(l);
  }

  public void removeChangeListener(ChangeListener l) {
    slider.removeChangeListener(l);
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);

    slider.setEnabled(enabled);
    spinner.setEnabled(enabled);

    if (enabled) {

      JSpinner.NumberEditor editor = (JSpinner.NumberEditor) spinner.getEditor();
      editor.getTextField().setEditable(true);
      Color editorBackground = editor.getTextField().getBackground();
      editor.getTextField().setEditable(false);
      editor.getTextField().setBackground(editorBackground);
    }
  }

}
