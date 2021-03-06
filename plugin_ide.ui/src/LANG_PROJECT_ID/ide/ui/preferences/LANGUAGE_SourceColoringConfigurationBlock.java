/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.ui.preferences;

import static melnorme.utilbox.core.CoreUtil.array;

import java.io.InputStream;

import LANG_PROJECT_ID.ide.ui.text.LANGUAGE_ColorPreferences;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.ide.ui.text.coloring.AbstractSourceColoringConfigurationBlock;
import melnorme.util.swt.jface.LabeledTreeElement;

public class LANGUAGE_SourceColoringConfigurationBlock extends AbstractSourceColoringConfigurationBlock {
	
	public LANGUAGE_SourceColoringConfigurationBlock(PreferencesPageContext prefContext) {
		super(prefContext);
	}
	
	@Override
	protected LabeledTreeElement[] createTreeElements() {
		return array(
			new SourceColoringCategory("Source", array(
				new SourceColoringElement("Default", LANGUAGE_ColorPreferences.DEFAULT),
				new SourceColoringElement("Keywords", LANGUAGE_ColorPreferences.KEYWORDS),
				new SourceColoringElement("Keywords - Literals", LANGUAGE_ColorPreferences.KEYWORDS_VALUES),
				new SourceColoringElement("Strings", LANGUAGE_ColorPreferences.STRINGS),
				new SourceColoringElement("Characters", LANGUAGE_ColorPreferences.CHARACTER)
			)),
			new SourceColoringCategory("Comments", array(
				new SourceColoringElement("Comment", LANGUAGE_ColorPreferences.COMMENTS),
				new SourceColoringElement("Doc Comment", LANGUAGE_ColorPreferences.DOC_COMMENTS)
			))
		);
	}
	
	private static final String PREVIEW_FILE_NAME = "SourceColoringPreviewFile.lang";
	
	@Override
	protected InputStream getPreviewContentAsStream() {
		return getClass().getResourceAsStream(PREVIEW_FILE_NAME);
	}
	
}