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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;
import org.eclipse.ecf.osgi.serviceregistry.launch.BundleFinder;
import org.eclipse.ecf.osgi.serviceregistry.launch.ClasspathBundleFinder;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class ConnectServiceRegistryFrameworkFactory implements FrameworkFactory {

	public Framework newFramework(Map<String, String> configuration) {
		ServiceRegistryFactory serviceRegistryFactory = ServiceLoader
				.load(ServiceRegistryFactory.class).iterator().next();
		ConnectServiceRegistry serviceRegistry = (ConnectServiceRegistry) serviceRegistryFactory
				.newServiceRegistry(configuration);
		List<BundleFinder> bsList = new ArrayList<BundleFinder>();
		for (Iterator<BundleFinder> i = ServiceLoader.load(BundleFinder.class)
				.iterator(); i.hasNext();)
			bsList.add(i.next());
		if (bsList.size() == 0)
			bsList.add(new ClasspathBundleFinder());
		return new ConnectServiceRegistryFramework(configuration, serviceRegistry,
				bsList);
	}

}
