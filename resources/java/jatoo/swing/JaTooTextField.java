/*
 * Copyright (C) 2011 Cristian Sulea ( http://cristian.sulea.net )
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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Improved {@link JTextField} with many new things like maximum length, allowed
 * characters, numeric values, negative sign, etc..
 * 
 * @author Cristian Sulea ( http://cristian.sulea.net )
 * @version 2.0 January 6, 2014
 */
@SuppressWarnings("serial")
public class JaTooTextField extends JTextField {

	private FocusListener listenerSelectAllOnFocus;

	private int maximumCharacters = Integer.MAX_VALUE;
	private String validCharacters;
	private String invalidCharacters;

	private DecimalFormat nf;
	private String decimalSeparator;
	private boolean isNegativeAllowed;
	private boolean isCapsOn;

	private String placeholder;

	public JaTooTextField() {
		_init(null);
	}

	public JaTooTextField(String t) {
		_init(t);
	}

	public JaTooTextField(double d) {
		_init(null);
		setNumeric(true);
		setText(d);
	}

	public JaTooTextField(int i, boolean isNegativeAllowed) {
		_init(null);
		setNumericInteger(true);
		setText(i);
		setNegativeAllowed(isNegativeAllowed);
	}

	public JaTooTextField(int i) {
		this(i, false);
	}

	private void _init(String text) {

		setNumeric(false);
		setNegativeAllowed(true);

		//
		// this must be AFTER all other settings to be able to apply them

		setDocument(new AdvancedTextFieldDocument());

		//
		// this must be at the end

		if (text != null) {
			setText(text);
		}

		//
		// don't forget to update the UI

		updateUI();
	}

	/**
	 * Force this component to select the entire text every time he gains the
	 * focus.
	 */
	public synchronized void setSelectAllOnFocus(boolean b) {

		if (b) {

			if (listenerSelectAllOnFocus == null) {
				listenerSelectAllOnFocus = JaTooSwingUtils.setSelectAllOnFocus(this);
			}

			addFocusListener(listenerSelectAllOnFocus);
		}

		else {
			removeFocusListener(listenerSelectAllOnFocus);
		}
	}

	@Override
	public String getText() {
		return super.getText();
	}

	@Override
	public void setText(String t) {
		if (t == null || t.length() == 0) {
			super.setText("");
			return;
		}

		if (isNumeric()) {
			boolean ok = true;
			try {
				String t2 = nf.format(nf.parse(t));
				ok = t2.length() == t.length();
			} catch (ParseException e) {
				ok = false;
			}
			if (!ok) {
				throw new IllegalArgumentException("Wrong text passed to a numeric field: '" + t + "'.");
			}
		}
		super.setText(t);
		setCaretPosition(getText().length());
	}

	public void setText(int i) {
		checkIfIsNumeric();
		setText(nf.format(i));
	}

	public void setText(double d) {
		checkIfIsNumeric();
		setText(nf.format(d));
	}

	public int getTextAsInteger() throws ParseException {
		checkIfIsNumeric();
		return nf.parse(getText()).intValue();
	}

	public int getTextAsInteger(int defaultValue) {
		checkIfIsNumeric();
		try {
			return getTextAsInteger();
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public double getTextAsDouble() throws ParseException {
		checkIfIsNumeric();
		return nf.parse(getText()).doubleValue();
	}

	public double getTextAsDouble(double defaultValue) {
		checkIfIsNumeric();
		try {
			return getTextAsDouble();
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public int getMaximumCharacters() {
		return maximumCharacters;
	}

	public void setMaximumCharacters(int i) {
		this.maximumCharacters = i;
		setColumns(i);
	}

	public String getValidCharacters() {
		return validCharacters;
	}

	public void setValidCharacters(String validCharacters) {
		this.validCharacters = validCharacters;
	}

	public String getInvalidCharacters() {
		return invalidCharacters;
	}

	public void setInvalidCharacters(String invalidCharacters) {
		this.invalidCharacters = invalidCharacters;
	}

	public boolean isNumeric() {
		return nf != null;
	}

	public void setNumeric(boolean b) {

		if (b) {

			nf = (DecimalFormat) NumberFormat.getNumberInstance();

			nf.setGroupingUsed(false);

			nf.setMaximumIntegerDigits(Integer.toString(Integer.MAX_VALUE).length());
			nf.setMaximumFractionDigits(2);

			decimalSeparator = String.valueOf(nf.getDecimalFormatSymbols().getDecimalSeparator());

			setHorizontalAlignment(TRAILING);
		}

		else {

			nf = null;
			decimalSeparator = null;

			setHorizontalAlignment(LEADING);
		}
	}

	public void setNumericInteger(boolean b) {

		if (b) {

			setNumeric(true);
			setValidCharacters("-0123456789");

		} else {

			setNumeric(false);
			setValidCharacters(null);
		}
	}

	public int getMaximumIntegerDigits() {
		checkIfIsNumeric();
		return nf.getMaximumIntegerDigits();
	}

	public void setMaximumIntegerDigits(int i) {
		checkIfIsNumeric();
		nf.setMaximumIntegerDigits(i);
	}

	public int getMaximumFractionDigits() {
		checkIfIsNumeric();
		return nf.getMaximumFractionDigits();
	}

	public void setMaximumFractionDigits(int i) {
		checkIfIsNumeric();
		nf.setMaximumFractionDigits(i);
	}

	public boolean isNegativeAllowed() {
		return isNegativeAllowed;
	}

	public void setNegativeAllowed(boolean b) {
		this.isNegativeAllowed = b;
	}

	public boolean isCapsOn() {
		return isCapsOn;
	}

	public void setCapsOn(boolean isCapsOn) {
		this.isCapsOn = isCapsOn;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (placeholder != null && placeholder.length() > 0) {
			if (getText().length() == 0) {

				Graphics2D g2 = (Graphics2D) g.create();

				Insets insets = getInsets();

				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getDisabledTextColor());
				g2.drawString(placeholder, insets.left, g2.getFontMetrics().getMaxAscent() + insets.top);
			}
		}
	}

	//

	private void checkIfIsNumeric() {
		if (!isNumeric()) {
			throw new NotNumericException();
		}
	}

	private class AdvancedTextFieldDocument extends PlainDocument {

		@Override
		public void insertString(int offset, String str, AttributeSet as) throws BadLocationException {

			if (str != null) {

				if (validCharacters != null) {
					for (int i = 0; i < str.length(); i++) {
						if (validCharacters.indexOf(str.charAt(i)) == -1) {
							Toolkit.getDefaultToolkit().beep();
							return;
						}
					}
				}

				if (invalidCharacters != null) {
					for (int i = 0; i < str.length(); i++) {
						if (invalidCharacters.indexOf(str.charAt(i)) != -1) {
							Toolkit.getDefaultToolkit().beep();
							return;
						}
					}
				}

				if (getLength() + str.length() > getMaximumCharacters()) {
					Toolkit.getDefaultToolkit().beep();
					return;
				}

				String sign = "";

				if (isNumeric()) {

					if (str.startsWith("-")) {

						sign = "-";

						if (str.length() > 1) {
							str = str.substring(1);
						} else {
							str = "";
						}

						if (offset != 0 || !isNegativeAllowed()) {
							Toolkit.getDefaultToolkit().beep();
							return;
						}
					}

					if (str.contains(decimalSeparator) && nf.getMaximumFractionDigits() == 0) {
						Toolkit.getDefaultToolkit().beep();
						return;
					}

					String str2 = new StringBuilder(getText(0, getLength())).insert(offset, str).toString();

					if (str2.length() > 0) {

						str2 = sign + str2;

						int count = 0;
						int idx = 0;
						while ((idx = str2.indexOf(decimalSeparator, idx)) != -1) {
							count++;
							idx += decimalSeparator.length();
						}
						if (count > 1) {
							Toolkit.getDefaultToolkit().beep();
							return;
						}

						int x = str2.endsWith(decimalSeparator) ? 1 : 0;

						try {
							if (str2.length() - x != nf.format(nf.parse(str2)).length()) {
								Toolkit.getDefaultToolkit().beep();
								return;
							}
						} catch (Exception e) {
							Toolkit.getDefaultToolkit().beep();
							return;
						}
					}

					// if (str2.endsWith(decimalSeparator) &&
					// nf.getMaximumFractionDigits() == 0) {
					// str2 = str2.substring(0, str2.length() - 1);
					// }

				}

				if (isCapsOn()) {
					str = str.toUpperCase();
				}

				super.insertString(offset, sign + str, as);
			}
		}
	}

	private class NotNumericException extends RuntimeException {
		public NotNumericException() {
			super("Field is not numeric.");
		}
	}

}
