package jatoo.system;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerSystemExit implements ActionListener {

	public static final ActionListener INSTANCE = new ActionListenerSystemExit();

	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}

}
