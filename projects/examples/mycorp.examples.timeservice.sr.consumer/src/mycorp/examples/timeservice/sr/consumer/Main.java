package mycorp.examples.timeservice.sr.consumer;

import java.io.IOException;
import java.util.ServiceLoader;

import mycorp.examples.timeservice.ITimeService;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;
import org.eclipse.ecf.osgi.services.remoteserviceadmin.DebugRemoteServiceAdminListener;
import org.eclipse.ecf.osgi.services.remoteserviceadmin.EndpointDescriptionReader;
import org.osgi.framework.ServiceReference;
import org.osgi.service.remoteserviceadmin.EndpointDescription;
import org.osgi.service.remoteserviceadmin.RemoteServiceAdmin;
import org.osgi.service.remoteserviceadmin.RemoteServiceAdminListener;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

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

	private static ServiceTrackerCustomizer<ITimeService, ITimeService> customizer = new ServiceTrackerCustomizer<ITimeService, ITimeService>() {
		// This method is called when the remote service is discovered and
		// imported via RSA
		public ITimeService addingService(
				ServiceReference<ITimeService> reference) {
			// Get time service
			ITimeService timeService = serviceRegistry.getBundleContext()
					.getService(reference);
			// call it's method! This is the remote call
			Long remoteTime = timeService.getCurrentTime();
			System.out
					.println("Called timeservice.getCurrentTime() with result="
							+ remoteTime);
			return timeService;
		}

		public void modifiedService(ServiceReference<ITimeService> reference,
				ITimeService service) {
		}

		public void removedService(ServiceReference<ITimeService> reference,
				ITimeService service) {
		}
	};

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

		// register debug listener. This is only needed so that the
		// remote service registration information is printed to console
		serviceRegistry.registerService(RemoteServiceAdminListener.class,
				new DebugRemoteServiceAdminListener(), null);

		// setup and open ServiceTracker. See the customizer member variable
		// above
		// The addingService method is called when the remote service is
		// imported
		ServiceTracker<ITimeService, ITimeService> serviceTracker = new ServiceTracker<ITimeService, ITimeService>(
				serviceRegistry.getBundleContext(), ITimeService.class,
				customizer);
		serviceTracker.open();

		// Read endpoint description from file
		EndpointDescription ed = readEndpointDescription("timeserviceendpointdescription.xml");
		// Get RSA instance
		RemoteServiceAdmin rsa = getRSA();
		// Import remote service. This will result in the remote service proxy
		// being created
		// and added to the local serviceRegistry. This will further result in
		// the customizer.addingService method to be called.
		rsa.importService(ed);

	}

}
