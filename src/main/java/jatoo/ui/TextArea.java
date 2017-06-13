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

import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;

/**
 * Improved {@link JTextArea} with many new things like maximum lines.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 3.0, May 20, 2014
 */
@SuppressWarnings("serial")
public class TextArea extends JTextArea {

  private int maximumLines;

  private String highlightPattern;
  private boolean highlightIgnoreCase = false;
  private DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

  public TextArea() {
    super();
  }

  public TextArea(Document doc, String text, int rows, int columns) {
    super(doc, text, rows, columns);
  }

  public TextArea(Document doc) {
    super(doc);
  }

  public TextArea(int rows, int columns) {
    super(rows, columns);
  }

  public TextArea(String text, int rows, int columns) {
    super(text, rows, columns);
  }

  public TextArea(String text) {
    super(text);
  }

  public void setMaximumLines(int maximumLines) {
    this.maximumLines = maximumLines;
    checkForMaximumLines();
  }

  public int getMaximumLines() {
    return maximumLines;
  }

  public void setHighlightPattern(String highlightPattern) {
    setHighlightPattern(highlightPattern, false);
  }

  public void setHighlightPattern(String highlightPattern, boolean highlightIgnoreCase) {
    this.highlightPattern = highlightPattern;
    this.highlightIgnoreCase = highlightIgnoreCase;
    checkForHighlight();
  }

  public String getHighlightPattern() {
    return highlightPattern;
  }

  public boolean isHighlightIgnoreCase() {
    return highlightIgnoreCase;
  }

  public void setHighlightColor(Color highlightColor) {
    this.highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(highlightColor);
    checkForHighlight();
  }

  public Color getHighlightColor() {
    return highlightPainter.getColor();
  }

  @Override
  public void append(String str) {
    super.append(str);

    checkForMaximumLines();
    checkForHighlight();
  }

  private void checkForMaximumLines() {

    if (maximumLines > 0) {

      try {

        int lines = getLineCount();

        if (lines > maximumLines) {

          int linesToRemove = lines - maximumLines - 1;
          int lengthToRemove = getLineStartOffset(linesToRemove);

          ((AbstractDocument) getDocument()).remove(0, lengthToRemove);
        }
      }

      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void checkForHighlight() {
    if (highlightPattern != null) {
      UIUtils.highlight(this, highlightPattern, highlightPainter, highlightIgnoreCase);
    }
  }

}
