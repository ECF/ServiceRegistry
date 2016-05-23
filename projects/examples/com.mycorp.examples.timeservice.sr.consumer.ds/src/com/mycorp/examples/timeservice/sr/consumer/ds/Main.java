/*******************************************************************************
 * Copyright (c) 2016 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package com.mycorp.examples.timeservice.sr.consumer.ds;

import java.util.ServiceLoader;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;
import org.eclipse.ecf.osgi.services.remoteserviceadmin.EndpointDescriptionReader;
import org.osgi.service.remoteserviceadmin.EndpointDescription;
import org.osgi.service.remoteserviceadmin.RemoteServiceAdmin;
import org.osgi.util.tracker.ServiceTracker;

public class Main {

	public static void main(String[] args) throws Exception {
		// Create service registry
		ServiceRegistry serviceRegistry = ServiceLoader.load(ServiceRegistryFactory.class)
				.iterator().next().newServiceRegistry(null);

		// Get RSA instance
		ServiceTracker<RemoteServiceAdmin, RemoteServiceAdmin> rsaTracker = new ServiceTracker<RemoteServiceAdmin, RemoteServiceAdmin>(
				serviceRegistry.getBundleContext(), RemoteServiceAdmin.class,
				null);
		rsaTracker.open();
		RemoteServiceAdmin rsa = rsaTracker.getService();
		rsaTracker.close();
		
		// Read endpoint description from file
		EndpointDescription ed = new EndpointDescriptionReader()
		.readEndpointDescriptions(Main.class
				.getResourceAsStream("timeserviceendpointdescription.xml"))[0];
		
		// Import the remote service
		rsa.importService(ed);

	}

}
