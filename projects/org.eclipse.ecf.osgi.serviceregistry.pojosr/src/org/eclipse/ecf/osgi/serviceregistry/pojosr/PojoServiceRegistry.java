/*******************************************************************************
 * Copyright (c) 2015 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.osgi.serviceregistry.pojosr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

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

import de.kalpatec.pojosr.framework.PojoSR;

public class PojoServiceRegistry implements ServiceRegistry {

	private final PojoSR pojoSR;

	public PojoServiceRegistry(PojoSR pojoSR) {
		this.pojoSR = pojoSR;
	}

	List<de.kalpatec.pojosr.framework.launch.BundleDescriptor> convertBundleDescriptors(
			Collection<BundleDescriptor> bundles) {
		List<de.kalpatec.pojosr.framework.launch.BundleDescriptor> results = new ArrayList<de.kalpatec.pojosr.framework.launch.BundleDescriptor>();
		for (BundleDescriptor bd : bundles)
			results.add(new de.kalpatec.pojosr.framework.launch.BundleDescriptor(
					bd.getClassLoader(), bd.getURL(), bd.getHeaders()));
		return results;
	}

	public void initBundles(Collection<BundleDescriptor> bundles)
			throws BundleException {
		try {
			pojoSR.startBundles(convertBundleDescriptors(bundles));
		} catch (Exception e) {
			throw new BundleException("Could not start bundles", e);
		}
	}

	@Override
	public BundleContext getBundleContext() {
		return pojoSR.getBundleContext();
	}

	@Override
	public void addServiceListener(ServiceListener listener, String filter)
			throws InvalidSyntaxException {
		pojoSR.addServiceListener(listener, filter);
	}

	@Override
	public void addServiceListener(ServiceListener listener) {
		pojoSR.addServiceListener(listener);
	}

	@Override
	public void removeServiceListener(ServiceListener listener) {
		pojoSR.removeServiceListener(listener);
	}

	@Override
	public ServiceRegistration<?> registerService(String[] clazzes,
			Object service, Dictionary<String, ?> properties) {
		return pojoSR.registerService(clazzes, service, properties);
	}

	@Override
	public ServiceRegistration<?> registerService(String clazz, Object service,
			Dictionary<String, ?> properties) {
		return pojoSR.registerService(clazz, service, properties);
	}

	@Override
	public <S> ServiceRegistration<S> registerService(Class<S> clazz,
			S service, Dictionary<String, ?> properties) {
		return pojoSR.registerService(clazz, service, properties);
	}

	@Override
	public <S> ServiceRegistration<S> registerService(Class<S> clazz,
			ServiceFactory<S> factory, Dictionary<String, ?> properties) {
		return pojoSR.registerService(clazz, factory, properties);
	}

	@Override
	public ServiceReference<?>[] getServiceReferences(String clazz,
			String filter) throws InvalidSyntaxException {
		return pojoSR.getServiceReferences(clazz, filter);
	}

	@Override
	public ServiceReference<?>[] getAllServiceReferences(String clazz,
			String filter) throws InvalidSyntaxException {
		return pojoSR.getAllServiceReferences(clazz, filter);
	}

	@Override
	public ServiceReference<?> getServiceReference(String clazz) {
		return pojoSR.getServiceReference(clazz);
	}

	@Override
	public <S> ServiceReference<S> getServiceReference(Class<S> clazz) {
		return pojoSR.getServiceReference(clazz);
	}

	@Override
	public <S> Collection<ServiceReference<S>> getServiceReferences(
			Class<S> clazz, String filter) throws InvalidSyntaxException {
		return pojoSR.getServiceReferences(clazz, filter);
	}

	@Override
	public <S> ServiceObjects<S> getServiceObjects(ServiceReference<S> reference) {
		// XXX todo
		return null;
	}

	@Override
	public <S> S getService(ServiceReference<S> reference) {
		return pojoSR.getService(reference);
	}

	@Override
	public boolean ungetService(ServiceReference<?> reference) {
		return pojoSR.ungetService(reference);
	}

	@Override
	public void shutdown() throws BundleException {
		pojoSR.getBundleContext().getBundle().stop();
	}

}
