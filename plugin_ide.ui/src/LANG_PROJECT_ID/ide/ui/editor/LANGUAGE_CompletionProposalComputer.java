/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.ui.editor;

import melnorme.lang.ide.ui.text.completion.LangCompletionProposalComputer;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class LANGUAGE_CompletionProposalComputer extends LangCompletionProposalComputer {
	
	@Override
	protected Indexable<ToolCompletionProposal> doComputeProposals(SourceOpContext sourceContext, ICancelMonitor cm)
			throws CommonException, OperationCancellation {
		throw new CommonException("NOT IMPLEMENTED");
	}
	
}