package org.eclipse.ecf.osgi.serviceregistry.equinox;

import java.util.Map;
import java.util.ServiceLoader;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class EquinoxServiceRegistryFactory implements ServiceRegistryFactory {

	private static final long STOP_TIMEOUT = 3000;
	
	public ServiceRegistry newServiceRegistry(Map<String, String> configuration) {
		FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class).iterator().next();
		final Framework framework = frameworkFactory.newFramework(configuration);
		try {
			framework.start();
		} catch (BundleException e) {
			throw new RuntimeException("Could not start framework");
		}
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					framework.waitForStop(STOP_TIMEOUT);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}});
		t.start();
		return new EquinoxServiceRegistry(framework);
	}

}
