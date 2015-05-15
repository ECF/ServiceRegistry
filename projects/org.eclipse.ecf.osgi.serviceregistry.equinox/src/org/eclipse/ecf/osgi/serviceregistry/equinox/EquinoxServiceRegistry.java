package org.eclipse.ecf.osgi.serviceregistry.equinox;

import java.util.Collection;
import java.util.Dictionary;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceObjects;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.launch.Framework;

public class EquinoxServiceRegistry implements ServiceRegistry {

	private Framework framework;
	private BundleContext bc;
	
	public EquinoxServiceRegistry(Framework framework) {
		this.framework = framework;
		this.bc = framework.getBundleContext();
	}
	
	public BundleContext getBundleContext() {
		return bc;
	}

	public void addServiceListener(ServiceListener listener, String filter)
			throws InvalidSyntaxException {
		bc.addServiceListener(listener, filter);
	}

	public void addServiceListener(ServiceListener listener) {
		bc.addServiceListener(listener);
	}

	public void removeServiceListener(ServiceListener listener) {
		bc.removeServiceListener(listener);
	}

	public ServiceRegistration<?> registerService(String[] clazzes,
			Object service, Dictionary<String, ?> properties) {
		return bc.registerService(clazzes, service, properties);
	}

	public ServiceRegistration<?> registerService(String clazz, Object service,
			Dictionary<String, ?> properties) {
		return bc.registerService(clazz, service, properties);
	}

	public <S> ServiceRegistration<S> registerService(Class<S> clazz,
			S service, Dictionary<String, ?> properties) {
		return bc.registerService(clazz, service, properties);
	}

	public <S> ServiceRegistration<S> registerService(Class<S> clazz,
			ServiceFactory<S> factory, Dictionary<String, ?> properties) {
		return bc.registerService(clazz, factory, properties);
	}

	public ServiceReference<?>[] getServiceReferences(String clazz,
			String filter) throws InvalidSyntaxException {
		return bc.getServiceReferences(clazz, filter);
	}

	public ServiceReference<?>[] getAllServiceReferences(String clazz,
			String filter) throws InvalidSyntaxException {
		return bc.getAllServiceReferences(clazz, filter);
	}

	public ServiceReference<?> getServiceReference(String clazz) {
		return bc.getServiceReference(clazz);
	}

	public <S> ServiceReference<S> getServiceReference(Class<S> clazz) {
		return bc.getServiceReference(clazz);
	}

	public <S> Collection<ServiceReference<S>> getServiceReferences(
			Class<S> clazz, String filter) throws InvalidSyntaxException {
		return bc.getServiceReferences(clazz, filter);
	}

	public <S> ServiceObjects<S> getServiceObjects(ServiceReference<S> reference) {
		return bc.getServiceObjects(reference);
	}

	public <S> S getService(ServiceReference<S> reference) {
		return bc.getService(reference);
	}

	public boolean ungetService(ServiceReference<?> reference) {
		return bc.ungetService(reference);
	}

	public void shutdown() throws BundleException {
		this.framework.stop();
	}

}
