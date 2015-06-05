/*******************************************************************************
 * Copyright (c) 2015 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.test.osgi.serviceregistry;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class TestServiceRegistry {

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {

		final ServiceRegistryFactory srFactory = ServiceLoader
				.load(ServiceRegistryFactory.class).iterator().next();
		final ServiceRegistry serviceRegistry = srFactory
				.newServiceRegistry(new HashMap<String, String>());

		ServiceTracker<Map, Map> serviceTracker = new ServiceTracker<Map, Map>(
				serviceRegistry.getBundleContext(), Map.class,
				new ServiceTrackerCustomizer<Map, Map>() {

					@Override
					public Map addingService(ServiceReference<Map> reference) {
						System.out.println("addingService=" + reference);
						return serviceRegistry.getService(reference);
					}

					@Override
					public void modifiedService(
							ServiceReference<Map> reference, Map service) {
						// TODO Auto-generated method stub

					}

					@Override
					public void removedService(ServiceReference<Map> reference,
							Map service) {
						System.out.println("removedService=" + reference);
					}
				});
		serviceTracker.open();

		// Now register a map
		ServiceRegistration reg = serviceRegistry.registerService(
				new String[] { Map.class.getName() }, new HashMap(), null);

		System.out.println("Map service registration is: "+reg);
		
		// Wait for a little bit 
		synchronized (srFactory) {
			try {
				System.out.println("Waiting 5 seconds...");
				srFactory.wait(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		reg.unregister();
		
		serviceRegistry.shutdown();
	}

}
