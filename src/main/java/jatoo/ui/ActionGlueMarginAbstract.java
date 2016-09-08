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
