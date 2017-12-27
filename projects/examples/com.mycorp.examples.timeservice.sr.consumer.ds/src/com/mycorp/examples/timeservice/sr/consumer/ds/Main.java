/*******************************************************************************
 * Copyright (c) 2017 Composent, Inc. and others. All rights reserved. This
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
import org.eclipse.ecf.osgi.services.remoteserviceadmin.DebugRemoteServiceAdminListener;
import org.eclipse.ecf.osgi.services.remoteserviceadmin.EndpointDescriptionReader;
import org.osgi.service.remoteserviceadmin.EndpointDescription;
import org.osgi.service.remoteserviceadmin.ImportRegistration;
import org.osgi.service.remoteserviceadmin.RemoteServiceAdmin;
import org.osgi.service.remoteserviceadmin.RemoteServiceAdminListener;

import com.mycorp.examples.timeservice.ITimeService;

public class Main {

	public static void main(String[] args) throws Exception {
		// Create service registry
		ServiceRegistry serviceRegistry = ServiceLoader.load(ServiceRegistryFactory.class).iterator().next()
				.newServiceRegistry(null);

		// This service registration produces RSA export registration info to console
		serviceRegistry.registerService(RemoteServiceAdminListener.class, new DebugRemoteServiceAdminListener(), null);

		// Get RemoteServiceAdmin instance
		RemoteServiceAdmin rsa = serviceRegistry
				.getService(serviceRegistry.getServiceReference(RemoteServiceAdmin.class));

		// Read endpoint description from edef file
		EndpointDescription ed = new EndpointDescriptionReader()
				.readEndpointDescriptions(Main.class.getResourceAsStream("timeserviceendpointdescription.xml"))[0];

		// Import the remote service using endpoint description
		System.out.println("Consumer:  Importing remote time service");
		ImportRegistration reg = rsa.importService(ed);
		// Check for import error
		Throwable t = reg.getException();
		if (t != null) {
			// if import exception print stack trace and throw
			t.printStackTrace();
			throw new Exception("Could not import remote service", t);
		}
		// No import error, so we are good to use the imported service
		System.out.println("Consumer:  Remote time service imported. Import Registration=" + reg);
		// Get imported service instance from service registry with registration
		ITimeService ts = (ITimeService) serviceRegistry.getService(reg.getImportReference().getImportedService());
		System.out.print("Consumer:  Calling remote ITimeService.getCurrentTime()=" + ts.getCurrentTime());
	}

}
