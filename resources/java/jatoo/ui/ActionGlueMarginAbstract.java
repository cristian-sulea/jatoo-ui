package jatoo.ui;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public abstract class ActionGlueMarginAbstract extends AbstractAction {

	private Component component;
	private int gap;

	public ActionGlueMarginAbstract(Component component, int gap) {
		this.component = component;
		this.gap = gap;
	}

	public ActionGlueMarginAbstract(Component component) {
		this(component, 0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		GraphicsDevice[] gds = ge.getScreenDevices();

//		for (GraphicsDevice gd : gds) {
//			if (component.getGraphicsConfiguration().getDevice().equals(gd)) {
//				glue(component, gap, gd.getDefaultConfiguration().getBounds());
//				break;
//			}
//		}
		
		glue(component, gap, component.getGraphicsConfiguration().getBounds());
	}

	protected abstract void glue(Component component, int gap, Rectangle screenDeviceBounds);

}
