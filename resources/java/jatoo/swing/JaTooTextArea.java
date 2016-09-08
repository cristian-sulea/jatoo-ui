/*
 * Copyright (C) 2013 Cristian Sulea ( http://cristian.sulea.net )
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

package jatoo.swing;

import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;

/**
 * Improved {@link JTextArea} with many new things like maximum lines.
 * 
 * @author Cristian Sulea ( http://cristian.sulea.net )
 * @version 2.1 January 12, 2014
 */
@SuppressWarnings("serial")
public class JaTooTextArea extends JTextArea {

	private int maximumLines;

	private String highlightPattern;
	private boolean highlightIgnoreCase = false;
	private DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

	public JaTooTextArea() {
		super();
	}

	public JaTooTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
	}

	public JaTooTextArea(Document doc) {
		super(doc);
	}

	public JaTooTextArea(int rows, int columns) {
		super(rows, columns);
	}

	public JaTooTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
	}

	public JaTooTextArea(String text) {
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
			JaTooSwingUtils.highlight(this, highlightPattern, highlightPainter, highlightIgnoreCase);
		}
	}

}
