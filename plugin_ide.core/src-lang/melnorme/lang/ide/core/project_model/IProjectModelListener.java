/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.project_model;


public interface IProjectModelListener<INFO> {
	
	void notifyUpdateEvent(UpdateEvent<INFO> updateEvent);
	
	@SuppressWarnings("unchecked")
	default <INFO_SUBTYPE extends INFO> IProjectModelListener<INFO_SUBTYPE> castTypeParam() {
		return (IProjectModelListener<INFO_SUBTYPE>) this;
	}
	
}