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
