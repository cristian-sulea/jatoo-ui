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

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;

/**
 * An abstract class to perform work in a background thread and display a dialog
 * with title, message and progress bar.
 * 
 * @author <a href="http://cristian.sulea.net" rel="author">Cristian Sulea</a>
 * @version 2.1, August 13, 2014
 */
public abstract class WorkerWithProgressDialog {

  private final JDialog dialog;

  private final JLabel titleLabel;
  private final JProgressBar progressBar;
  private final JLabel messageLabel;

  private final JButton cancelButton;

  private boolean cancelled;

  public WorkerWithProgressDialog(final JComponent owner, final String title) {
    this(owner.getTopLevelAncestor(), title);
  }

  public WorkerWithProgressDialog(final JFrame owner, final String title) {
    this((Container) owner, title);
  }

  public WorkerWithProgressDialog(final JDialog owner, final String title) {
    this((Container) owner, title);
  }

  private WorkerWithProgressDialog(final Container owner, final String title) {

    //
    // initialize the components

    if (owner instanceof JFrame) {
      dialog = new JDialog((JFrame) owner, ((JFrame) owner).getTitle(), true);
    } else {
      dialog = new JDialog((JDialog) owner, ((JDialog) owner).getTitle(), true);
    }

    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    dialog.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        cancel();
      }
    });

    dialog.setResizable(false);

    titleLabel = new JLabel(title);
    titleLabel.setIcon(UITheme.getIcon(WorkerWithProgressDialog.class, "Icon"));

    progressBar = new JProgressBar(0, 100);

    messageLabel = new JLabel();

    cancelButton = new JButton(UITheme.getText(WorkerWithProgressDialog.class, "Cancel"));
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancel();
      }
    });

    //
    // layout

    final JPanel cp = new JPanel(new MigLayout("fill, wrap 1", "[fill, grow]"));
    cp.add(titleLabel);
    cp.add(progressBar);
    cp.add(messageLabel);
    cp.add(cancelButton, "tag cancel");

    dialog.setContentPane(cp);

    //
    // trick to increase a little the size of the dialog

    setMessage("Cam cat sa fie mesajul asta de lund? Oare o fi de ajuns?");
    cp.setPreferredSize(cp.getPreferredSize());
    setMessage(" ");

    //
    // pack the window and move'it relative to owner

    dialog.pack();
    dialog.setLocationRelativeTo(owner);
  }

  public final void start() {

    //
    // start the work ( !!! before showing dialog )

    new Thread() {
      public void run() {

        cancelled = false;

        work();

        dialog.setVisible(false);
        dialog.dispose();

        done();
      }
    }.start();

    //
    // show the dialog ( !!! after starting the work )

    dialog.setVisible(true);
  }

  protected synchronized final void cancel() {
    this.cancelled = true;
  }

  protected synchronized final boolean isCancelled() {
    return cancelled;
  }

  protected abstract void work();

  protected abstract void done();

  protected final void setProgress(int progress) {
    progressBar.setValue(progress);
  }

  protected final void setMessage(String message) {
    messageLabel.setText(message);
  }

  protected final void setProgress(int progress, String message) {
    setProgress(progress);
    setMessage(message);
  }

}
