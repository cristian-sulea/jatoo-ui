package jatoo.ui;

import java.awt.Component;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class ActionGlueMarginTop extends ActionGlueMarginAbstract {

	public ActionGlueMarginTop(Component component, int gap) {
		super(component, gap);
	}

	public ActionGlueMarginTop(Component component) {
		super(component);
	}

	@Override
	protected void glue(Component component, int gap, Rectangle screenDeviceBounds) {
		component.setLocation(component.getX(), screenDeviceBounds.y + gap);
	}

}
