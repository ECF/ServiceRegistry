/*******************************************************************************
 * Copyright (c) 2016 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package mycorp.examples.timeservice.sr.host;

import mycorp.examples.timeservice.ITimeService;

public class TimeServiceImpl implements ITimeService {

	/**
	 * Implementation of my time service.
	 */
	public Long getCurrentTime() {
		// Print out to host std out that a call to this service was received.
		System.out
				.println("TimeServiceImpl.  Received call to getCurrentTime()");
		return new Long(System.currentTimeMillis());
	}

}
