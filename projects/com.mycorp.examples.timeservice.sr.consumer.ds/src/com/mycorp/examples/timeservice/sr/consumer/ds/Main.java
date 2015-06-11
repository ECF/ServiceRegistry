package com.mycorp.examples.timeservice.sr.consumer.ds;

import java.util.ServiceLoader;

import org.eclipse.ecf.core.util.PlatformHelper;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;
import org.eclipse.ecf.osgi.services.remoteserviceadmin.EndpointDescriptionReader;
import org.osgi.service.remoteserviceadmin.EndpointDescription;
import org.osgi.service.remoteserviceadmin.RemoteServiceAdmin;
import org.osgi.util.tracker.ServiceTracker;

public class Main {

	private static ServiceRegistry serviceRegistry;

	public static void main(String[] args) throws Exception {
		// Create service registry
		serviceRegistry = ServiceLoader.load(ServiceRegistryFactory.class)
				.iterator().next().newServiceRegistry(null);

		PlatformHelper.getPlatformAdapterManager();

		// Read endpoint description from file
		EndpointDescription[] eds = new EndpointDescriptionReader()
				.readEndpointDescriptions(Main.class
						.getResourceAsStream("timeserviceendpointdescription.xml"));

		// Get RSA instance
		ServiceTracker<RemoteServiceAdmin, RemoteServiceAdmin> rsaTracker = new ServiceTracker<RemoteServiceAdmin, RemoteServiceAdmin>(
				serviceRegistry.getBundleContext(), RemoteServiceAdmin.class,
				null);
		rsaTracker.open();
		RemoteServiceAdmin rsa = rsaTracker.getService();
		rsaTracker.close();

		// Import remote service
		rsa.importService(eds[0]);

		Object w = new Object();
		synchronized (w) {
			// wait some time
			try {
				w.wait(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		serviceRegistry.shutdown();

	}

}
