package jatoo.ui;

import java.awt.Component;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class ActionGlueMarginRight extends ActionGlueMarginAbstract {

	public ActionGlueMarginRight(Component component, int gap) {
		super(component, gap);
	}

	public ActionGlueMarginRight(Component component) {
		super(component);
	}

	@Override
	protected void glue(Component component, int gap, Rectangle screenDeviceBounds) {
		component.setLocation(screenDeviceBounds.width + screenDeviceBounds.x - component.getWidth() + gap, component.getY());
	}

}
