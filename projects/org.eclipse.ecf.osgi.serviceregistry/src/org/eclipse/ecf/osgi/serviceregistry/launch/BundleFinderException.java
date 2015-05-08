/*******************************************************************************
 * Copyright (c) 2015 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.osgi.serviceregistry.launch;

public class BundleFinderException extends Exception {

	private static final long serialVersionUID = -5947423966161678843L;

	public BundleFinderException() {
	}

	public BundleFinderException(String message) {
		super(message);
	}

	public BundleFinderException(Throwable cause) {
		super(cause);
	}

	public BundleFinderException(String message, Throwable cause) {
		super(message, cause);
	}

	public BundleFinderException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
