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

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

/**
 * An {@link OutputStream} that will dump everything in a {@link JTextArea}.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 3.0, June 23, 2014
 */
public class TextAreaOutputStream extends OutputStream {

  private byte buffer[];
  private int count;

  private JTextArea textArea;

  public TextAreaOutputStream(JTextArea textArea) {
    this(812, textArea);
  }

  public TextAreaOutputStream(int size, JTextArea textArea) {

    if (size <= 0) {
      throw new IllegalArgumentException("buffer size <= 0");
    }

    this.buffer = new byte[size];
    this.textArea = textArea;
  }

  /** Flush the internal buffer */
  private void flushBuffer() throws IOException {
    if (count > 0) {
      textArea.append(new String(buffer, 0, count));
      count = 0;
    }
  }

  public synchronized void write(int b) throws IOException {

    if (count >= buffer.length) {
      flushBuffer();
    }

    buffer[count++] = (byte) b;
  }

  public synchronized void write(byte b[], int off, int len) throws IOException {

    if (len >= buffer.length) {
      flushBuffer();
      textArea.append(new String(b, off, len));
      return;
    }

    if (len > buffer.length - count) {
      flushBuffer();
    }

    System.arraycopy(b, off, buffer, count, len);

    count += len;
  }

  public synchronized void flush() throws IOException {
    flushBuffer();
    textArea.setCaretPosition(textArea.getText().length());
  }

}
