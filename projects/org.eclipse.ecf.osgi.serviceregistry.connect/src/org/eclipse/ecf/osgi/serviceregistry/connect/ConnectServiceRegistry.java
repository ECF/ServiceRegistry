/*******************************************************************************
 * Copyright (c) 2015 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.osgi.serviceregistry.connect;

import static org.eclipse.ecf.osgi.serviceregistry.connect.ConnectServiceRegistryFactory.convertBundleDescriptors;

import java.util.Collection;
import java.util.Dictionary;

import org.apache.felix.connect.PojoSR;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.eclipse.ecf.osgi.serviceregistry.launch.BundleDescriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceObjects;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

public class ConnectServiceRegistry implements ServiceRegistry {

	private final PojoSR pojoSR;

	public ConnectServiceRegistry(PojoSR pojoSR) {
		this.pojoSR = pojoSR;
	}

	public void initBundles(Collection<BundleDescriptor> bundles)
			throws BundleException {
		try {
			pojoSR.startBundles(convertBundleDescriptors(bundles));
		} catch (Exception e) {
			throw new BundleException("Could not start bundles", e);
		}
	}

	public BundleContext getBundleContext() {
		return pojoSR.getBundleContext();
	}

	public void addServiceListener(ServiceListener listener, String filter)
			throws InvalidSyntaxException {
		pojoSR.addServiceListener(listener, filter);
	}

	public void addServiceListener(ServiceListener listener) {
		pojoSR.addServiceListener(listener);
	}

	public void removeServiceListener(ServiceListener listener) {
		pojoSR.removeServiceListener(listener);
	}

	public ServiceRegistration<?> registerService(String[] clazzes,
			Object service, Dictionary<String, ?> properties) {
		return pojoSR.registerService(clazzes, service, properties);
	}

	public ServiceRegistration<?> registerService(String clazz, Object service,
			Dictionary<String, ?> properties) {
		return pojoSR.registerService(clazz, service, properties);
	}

	public <S> ServiceRegistration<S> registerService(Class<S> clazz,
			S service, Dictionary<String, ?> properties) {
		return null;
		// pojoSR.registerService(clazz, service, properties);
	}

	public <S> ServiceRegistration<S> registerService(Class<S> clazz,
			ServiceFactory<S> factory, Dictionary<String, ?> properties) {
		return null;
		// pojoSR.registerService(new String[] { clazz.getName() }, factory,
		// properties);
	}

	public ServiceReference<?>[] getServiceReferences(String clazz,
			String filter) throws InvalidSyntaxException {
		return pojoSR.getServiceReferences(clazz, filter);
	}

	public ServiceReference<?>[] getAllServiceReferences(String clazz,
			String filter) throws InvalidSyntaxException {
		return null;
		// pojoSR.getAllServiceReferences(clazz, filter);
	}

	public ServiceReference<?> getServiceReference(String clazz) {
		return pojoSR.getServiceReference(clazz);
	}

	public <S> ServiceReference<S> getServiceReference(Class<S> clazz) {
		return null;
		// pojoSR.getServiceReference(clazz.getName());
	}

	public <S> Collection<ServiceReference<S>> getServiceReferences(
			Class<S> clazz, String filter) throws InvalidSyntaxException {
		return null;
		// pojoSR.getServiceReferences(clazz, filter);
	}

	public <S> ServiceObjects<S> getServiceObjects(ServiceReference<S> reference) {
		// XXX todo
		return null;
	}

	public <S> S getService(ServiceReference<S> reference) {
		return pojoSR.getService(reference);
	}

	public boolean ungetService(ServiceReference<?> reference) {
		return pojoSR.ungetService(reference);
	}

	public void shutdown() throws BundleException {
		pojoSR.getBundleContext().getBundle().stop();
	}

}
