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
import org.osgi.service.remoteserviceadmin.ImportRegistration;
import org.osgi.service.remoteserviceadmin.RemoteServiceAdmin;

import com.mycorp.examples.timeservice.ITimeService;

public class Main {

	public static void main(String[] args) throws Exception {
		// Create service registry
		ServiceRegistry serviceRegistry = ServiceLoader.load(ServiceRegistryFactory.class)
				.iterator().next().newServiceRegistry(null);

		// Get RSA instance
		RemoteServiceAdmin rsa = serviceRegistry.getService(serviceRegistry.getServiceReference(RemoteServiceAdmin.class));
		
		// Read endpoint description from file
		EndpointDescription ed = new EndpointDescriptionReader()
		.readEndpointDescriptions(Main.class
				.getResourceAsStream("timeserviceendpointdescription.xml"))[0];
		
		// Import the remote service using endpoint description
		ImportRegistration reg = rsa.importService(ed);
		System.out.println("import registration="+reg);
		// Check for import error
		Throwable t = reg.getException();
		if (t != null) {
			t.printStackTrace();
			throw new Exception("Could not import remote service",t);
		}
		// No error, so we are good to use the imported service
		// Get imported service instance
		ITimeService ts = (ITimeService)serviceRegistry.getService(reg.getImportReference().getImportedService());
		// And then call it
		System.out.println("time service result="+ts.getCurrentTime());
	}

}
