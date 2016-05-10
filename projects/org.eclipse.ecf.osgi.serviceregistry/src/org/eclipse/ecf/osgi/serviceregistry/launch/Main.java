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

import java.util.ServiceLoader;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;

public class Main {

	public static void main(String[] args) throws Exception {

		ServiceLoader.load(ServiceRegistryFactory.class).iterator().next().newServiceRegistry(null);

		Object w = new Object();
		synchronized (w) {
			// wait forever
			try {
				w.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
