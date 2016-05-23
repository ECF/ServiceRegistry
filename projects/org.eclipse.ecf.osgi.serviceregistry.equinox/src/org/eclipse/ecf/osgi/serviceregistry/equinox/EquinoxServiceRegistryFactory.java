package org.eclipse.ecf.osgi.serviceregistry.equinox;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryImpl;
import org.eclipse.ecf.osgi.serviceregistry.launch.BundleDescriptor;
import org.eclipse.ecf.osgi.serviceregistry.launch.BundleFinderException;
import org.eclipse.ecf.osgi.serviceregistry.launch.JarFileBundleFinder;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class EquinoxServiceRegistryFactory implements ServiceRegistryFactory {

	private static final long STOP_TIMEOUT = 3000;

	void installAndStartBundles(BundleContext bc, Collection<BundleDescriptor> descriptors) {
		List<Bundle> bundles = new ArrayList<Bundle>();
		for(BundleDescriptor bd: descriptors) {
				Bundle b;
				try {
					b = bc.installBundle(bd.getURL().toString());
					bundles.add(b);
				} catch (BundleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		for(Bundle b: bundles)
			try {
				b.start();
			} catch (BundleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	

	public ServiceRegistry newServiceRegistry(Map<String, ?> configuration) {
		FrameworkFactory frameworkFactory = ServiceLoader
				.load(FrameworkFactory.class).iterator().next();
		final Framework framework = frameworkFactory
				.newFramework(new HashMap<String, String>());
		try {
			framework.start();
		} catch (BundleException e) {
			throw new RuntimeException("Could not start framework");
		}
		
		// find and install bundles
		Collection<BundleDescriptor> descriptors = null;
		try {
			Path currentDir = Paths.get("");
			Path fullPath = currentDir.resolve("../../org.eclipse.ecf.osgi.serviceregistry.concierge/lib/bundles");
			descriptors = new JarFileBundleFinder.Builder().setStartPath(fullPath).findBundles();			
		} catch (BundleFinderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (descriptors != null)
			installAndStartBundles(framework.getBundleContext(), descriptors);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					framework.waitForStop(STOP_TIMEOUT);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
		return new ServiceRegistryImpl(framework);
	}

}
