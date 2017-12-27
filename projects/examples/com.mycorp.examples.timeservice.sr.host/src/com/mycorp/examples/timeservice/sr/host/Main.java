/*******************************************************************************
 * Copyright (c) 2017 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package com.mycorp.examples.timeservice.sr.host;

import java.util.Hashtable;
import java.util.ServiceLoader;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;
import org.eclipse.ecf.osgi.services.remoteserviceadmin.DebugRemoteServiceAdminListener;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.remoteserviceadmin.RemoteServiceAdminListener;

import com.mycorp.examples.timeservice.ITimeService;

public class Main {

	private static ServiceRegistry serviceRegistry;

	// Here is the ITimeService implementation class
	public static class TimeServiceImpl implements com.mycorp.examples.timeservice.ITimeService {

		@Override
		public Long getCurrentTime() {
			Long result = System.currentTimeMillis();
			System.out.println("getCurrentTime called on host.  Returning time=" + result);
			return result;
		}

	}

	public static void main(String[] args) throws Exception {
		// Create service registry using service loader and ServiceRegistryFactory
		serviceRegistry = ServiceLoader.load(ServiceRegistryFactory.class).iterator().next().newServiceRegistry(null);

		// This produces RSA export registration info to console for example
		serviceRegistry.registerService(RemoteServiceAdminListener.class, new DebugRemoteServiceAdminListener(), null);

		// Create and set service properties with RS-required values
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("service.exported.interfaces", "*");
		// For this example, will use the ecf.generic.server provider.
		// See http://wiki.eclipse.org/Distribution_Providers for list of OS providers
		props.put("service.exported.configs", "ecf.generic.server");
		// For this ecf.generic.server provider, hostname and port can be set via
		// properties
		props.put("ecf.generic.server.hostname", "localhost");
		props.put("ecf.generic.server.port", "3288");
		// Register with RS properties that will trigger RSA to export as part of
		// registration
		System.out.println("Service Host:  Registering remote implementation of ITimeService");
		ServiceRegistration<ITimeService> reg = serviceRegistry.registerService(ITimeService.class,
				new TimeServiceImpl(), props);
		System.out.println("Service Host:  Remote implementation of ITimeService registered.  Registration=" + reg);

		// Then main thread waits forever for remote service calls to
		// TimeServiceImpl.getCurrentTime()
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
