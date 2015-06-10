package com.mycorp.examples.timeservice.sr.host;

import java.util.ServiceLoader;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;

public class Main {

	private static ServiceRegistry serviceRegistry;

	public static void main(String[] args) throws Exception {

		serviceRegistry = ServiceLoader.load(ServiceRegistryFactory.class)
				.iterator().next().newServiceRegistry(null);

		Object w = new Object();
		synchronized (w) {
			// wait forever
			try {
				w.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
