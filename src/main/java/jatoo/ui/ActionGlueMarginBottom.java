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
