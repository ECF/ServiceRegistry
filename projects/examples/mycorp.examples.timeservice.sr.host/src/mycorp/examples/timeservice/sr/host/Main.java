package mycorp.examples.timeservice.sr.host;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;
import java.util.ServiceLoader;

import mycorp.examples.timeservice.ITimeService;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;
import org.eclipse.ecf.osgi.services.remoteserviceadmin.DebugRemoteServiceAdminListener;
import org.osgi.service.remoteserviceadmin.RemoteServiceAdminListener;

public class Main {

	private static ServiceRegistry serviceRegistry;

	public static void main(String[] args) {
		// Create service registry
		serviceRegistry = ServiceLoader.load(ServiceRegistryFactory.class)
				.iterator().next().newServiceRegistry(null);

		// register debug listener. This is only needed so that the
		// remote service registration information is printed to console
		serviceRegistry.registerService(RemoteServiceAdminListener.class,
				new DebugRemoteServiceAdminListener(), null);

		// Create remote service properties. These properties are as specified
		// by Chapter 100 in the OSGi R6 Remote Services specification (chap
		// 100).
		// http://www.osgi.org/Specifications/HomePage
		Dictionary<String, Object> props = createRemoteServiceProperties();

		// Create TimeServiceImpl and register as a remote service. This
		// will result in the TimeServiceImpl to be exported/made available
		// For access by ITimeService consumers
		serviceRegistry.registerService(ITimeService.class,
				new TimeServiceImpl(), props);

	}

	private static Dictionary<String, Object> createRemoteServiceProperties() {
		// This is the only required service property to trigger remote services
		Dictionary<String, Object> result = new Hashtable<String, Object>();
		result.put("service.exported.interfaces", "*");
		Properties props = System.getProperties();
		String config = props.getProperty("service.exported.configs");
		if (config != null) {
			result.put("service.exported.configs", config);
			String configProps = config + ".";
			for (Object k : props.keySet()) {
				if (k instanceof String) {
					String key = (String) k;
					if (key.startsWith(configProps)
							|| key.equals("ecf.exported.async.interfaces"))
						result.put(key, props.getProperty(key));
				}
			}
		}
		return result;
	}

}
