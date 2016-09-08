package jatoo.ui;

import java.awt.Component;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class ActionGlueMarginLeft extends ActionGlueMarginAbstract {

	public ActionGlueMarginLeft(Component component, int gap) {
		super(component, gap);
	}

	public ActionGlueMarginLeft(Component component) {
		super(component);
	}

	@Override
	protected void glue(Component component, int gap, Rectangle screenDeviceBounds) {
		component.setLocation(screenDeviceBounds.x + gap, component.getY());
	}

}
