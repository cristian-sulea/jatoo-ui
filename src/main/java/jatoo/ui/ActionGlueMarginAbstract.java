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
