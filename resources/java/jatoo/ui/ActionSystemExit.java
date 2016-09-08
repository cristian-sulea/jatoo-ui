package jatoo.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class ActionSystemExit extends AbstractAction {

	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}

}
