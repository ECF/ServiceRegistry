/*******************************************************************************
 * Copyright (c) 2015 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.osgi.serviceregistry;

import java.util.Collection;
import java.util.Dictionary;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceObjects;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

public interface ServiceRegistry {

	BundleContext getBundleContext();

	void addServiceListener(ServiceListener listener, String filter)
			throws InvalidSyntaxException;

	void addServiceListener(ServiceListener listener);

	void removeServiceListener(ServiceListener listener);

	ServiceRegistration<?> registerService(String[] clazzes, Object service,
			Dictionary<String, ?> properties);

	ServiceRegistration<?> registerService(String clazz, Object service,
			Dictionary<String, ?> properties);

	<S> ServiceRegistration<S> registerService(Class<S> clazz, S service,
			Dictionary<String, ?> properties);

	<S> ServiceRegistration<S> registerService(Class<S> clazz,
			ServiceFactory<S> factory, Dictionary<String, ?> properties);

	ServiceReference<?>[] getServiceReferences(String clazz, String filter)
			throws InvalidSyntaxException;

	ServiceReference<?>[] getAllServiceReferences(String clazz, String filter)
			throws InvalidSyntaxException;

	ServiceReference<?> getServiceReference(String clazz);

	<S> ServiceReference<S> getServiceReference(Class<S> clazz);

	<S> Collection<ServiceReference<S>> getServiceReferences(Class<S> clazz,
			String filter) throws InvalidSyntaxException;

	<S> ServiceObjects<S> getServiceObjects(ServiceReference<S> reference);

	<S> S getService(ServiceReference<S> reference);

	boolean ungetService(ServiceReference<?> reference);

	void shutdown() throws BundleException;
}
