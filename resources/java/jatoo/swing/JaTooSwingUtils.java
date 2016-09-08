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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.JTextComponent;

/**
 * A collection of utility methods for Swing.
 * 
 * @author Cristian Sulea ( http://cristian.sulea.net )
 * @version 2.1, February 12, 2014
 */
public class JaTooSwingUtils {

	/**
	 * Sets the location of the window relative to the associated screen.
	 * 
	 * @param window
	 *          the window to be moved
	 */
	public static void setWindowLocationRelativeToScreen(Window window) {

		GraphicsConfiguration gc = window.getGraphicsConfiguration();
		Rectangle gcBounds = gc.getBounds();

		Dimension windowSize = window.getSize();

		int dx = gcBounds.x + (gcBounds.width - windowSize.width) / 2;
		int dy = gcBounds.y + (gcBounds.height - windowSize.height) / 2;

		window.setLocation(dx, dy);
	}

	/**
	 * Sets the location of the window relative to the specified component.
	 * 
	 * @param window
	 *          the window to be moved
	 * @param component
	 *          the component in relation to which the window's location is
	 *          determined
	 */
	public static void setWindowLocationRelativeToComponent(Window window, Component component) {
		window.setLocationRelativeTo(component);
	}

	/**
	 * Adds to the provided text component a focus listener that will select all
	 * the text on focus gained.
	 * 
	 * @return the focus listener
	 */
	public static FocusListener setSelectAllOnFocus(final JTextComponent textComponent) {

		FocusListener focusListener = new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				textComponent.select(0, textComponent.getText().length());
			}
		};

		textComponent.addFocusListener(focusListener);

		return focusListener;
	}

	/**
	 * @see #highlight(JTextComponent, String, HighlightPainter, boolean)
	 */
	public static void highlight(JTextComponent textComponent, String pattern, Color color) {
		highlight(textComponent, pattern, new DefaultHighlighter.DefaultHighlightPainter(color), false);
	}

	/**
	 * @see #highlight(JTextComponent, String, HighlightPainter, boolean)
	 */
	public static void highlight(JTextComponent textComponent, String pattern, HighlightPainter highlightPainter) {
		highlight(textComponent, pattern, highlightPainter, false);
	}

	/**
	 * @see #highlight(JTextComponent, String, HighlightPainter, boolean)
	 */
	public static void highlight(JTextComponent textComponent, String pattern, Color color, boolean ignoreCase) {
		highlight(textComponent, pattern, new DefaultHighlighter.DefaultHighlightPainter(color), ignoreCase);
	}

	/**
	 * Highlights the patterns found in the text of the provided
	 * {@link JTextComponent}.
	 * 
	 * @param textComponent
	 * @param pattern
	 * @param highlightPainter
	 * @param ignoreCase
	 */
	public static void highlight(JTextComponent textComponent, String pattern, HighlightPainter highlightPainter, boolean ignoreCase) {

		String text = textComponent.getText();
		Highlighter highlighter = textComponent.getHighlighter();

		highlighter.removeAllHighlights();

		if (pattern.length() == 0) {
			return;
		}

		if (ignoreCase) {
			text = text.toLowerCase();
			pattern = pattern.toLowerCase();
		}

		int fromIndex = 0;

		while (true) {

			int index = text.indexOf(pattern, fromIndex);

			if (index < 0) {
				break;
			}

			int p0 = index;
			int p1 = index + pattern.length();

			try {

				if (p1 == text.length()) {
					highlighter.addHighlight(p0, p1 - 1, highlightPainter);
				}

				else {
					highlighter.addHighlight(p0, p1, highlightPainter);
				}
			}

			catch (BadLocationException e) {
				throw new RuntimeException("unexpected exception", e);
			}

			fromIndex = p1;
		}
	}

}
