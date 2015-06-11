package com.mycorp.examples.timeservice.sr.consumer.ds;

import java.io.IOException;
import java.util.ServiceLoader;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;
import org.eclipse.ecf.osgi.services.remoteserviceadmin.EndpointDescriptionReader;
import org.osgi.service.remoteserviceadmin.EndpointDescription;
import org.osgi.service.remoteserviceadmin.RemoteServiceAdmin;
import org.osgi.util.tracker.ServiceTracker;

public class Main {

	private static ServiceRegistry serviceRegistry;

	private static RemoteServiceAdmin getRSA() {
		ServiceTracker<RemoteServiceAdmin, RemoteServiceAdmin> rsaTracker = new ServiceTracker<RemoteServiceAdmin, RemoteServiceAdmin>(
				serviceRegistry.getBundleContext(), RemoteServiceAdmin.class,
				null);
		rsaTracker.open();
		RemoteServiceAdmin rsa = rsaTracker.getService();
		rsaTracker.close();
		return rsa;
	}

	private static EndpointDescription readEndpointDescription(String fileName)
			throws IOException {
		return new EndpointDescriptionReader()
				.readEndpointDescriptions(Main.class
						.getResourceAsStream(fileName))[0];
	}

	public static void main(String[] args) throws Exception {
		// Create service registry
		serviceRegistry = ServiceLoader.load(ServiceRegistryFactory.class)
				.iterator().next().newServiceRegistry(null);

		// Read endpoint description from file
		EndpointDescription ed = readEndpointDescription("timeserviceendpointdescription.xml");
		// Get RSA instance
		RemoteServiceAdmin rsa = getRSA();
		// Import remote service. This will discover and
		rsa.importService(ed);

	}

}
