package jatoo.ui;

import java.awt.Component;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class ActionGlueMarginBottom extends ActionGlueMarginAbstract {

	public ActionGlueMarginBottom(Component component, int gap) {
		super(component, gap);
	}

	public ActionGlueMarginBottom(Component component) {
		super(component);
	}

	@Override
	protected void glue(Component component, int gap, Rectangle screenDeviceBounds) {
		component.setLocation(component.getX(), screenDeviceBounds.height + screenDeviceBounds.y - component.getHeight() + gap);
	}

}
