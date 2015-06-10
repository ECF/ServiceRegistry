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

import java.util.Map;
import java.util.ServiceLoader;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class ConnectServiceRegistryFrameworkFactory implements FrameworkFactory {

	public Framework newFramework(Map<String, String> configuration) {
		ServiceRegistryFactory serviceRegistryFactory = ServiceLoader
				.load(ServiceRegistryFactory.class).iterator().next();
		ConnectServiceRegistry serviceRegistry = (ConnectServiceRegistry) serviceRegistryFactory
				.newServiceRegistry(configuration);
		return new ConnectServiceRegistryFramework(serviceRegistry);
	}

}
