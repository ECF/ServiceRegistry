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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;
import org.eclipse.ecf.osgi.serviceregistry.launch.BundleDescriptor;
import org.eclipse.ecf.osgi.serviceregistry.launch.BundleFinder;
import org.eclipse.ecf.osgi.serviceregistry.launch.ClasspathBundleFinder;
import org.apache.felix.connect.PojoSR;
import org.apache.felix.connect.launch.PojoServiceRegistryFactory;

public class ConnectServiceRegistryFactory implements ServiceRegistryFactory {

	static List<org.apache.felix.connect.launch.BundleDescriptor> convertBundleDescriptors(
			Collection<BundleDescriptor> bundles) {
		List<org.apache.felix.connect.launch.BundleDescriptor> results = new ArrayList<org.apache.felix.connect.launch.BundleDescriptor>();
		for (BundleDescriptor bd : bundles)
			results.add(new org.apache.felix.connect.launch.BundleDescriptor(bd
					.getClassLoader(), bd.getURL().toString(), bd.getHeaders()));
		return results;
	}

	public ServiceRegistry newServiceRegistry(final Map<String, ?> configuration) {
		try {
			Map<String, Object> pojoSRConfig = new HashMap<String, Object>();
			if (configuration == null) {
				// If configuration is null, we still setup bundles from
				// classpath
				pojoSRConfig.put(PojoServiceRegistryFactory.BUNDLE_DESCRIPTORS,
						convertBundleDescriptors(new ClasspathBundleFinder()
								.findBundles()));
			} else {
				List<org.apache.felix.connect.launch.BundleDescriptor> pojoSRDescriptors = new ArrayList<org.apache.felix.connect.launch.BundleDescriptor>();
				for (String ck : configuration.keySet()) {
					if (ck.startsWith(BUNDLE_FINDERS)) {
						@SuppressWarnings("unchecked")
						Collection<BundleFinder> bundleFinders = (Collection<BundleFinder>) configuration
								.get(ck);
						if (bundleFinders != null)
							for (BundleFinder finder : bundleFinders)
								pojoSRDescriptors
										.addAll(convertBundleDescriptors(finder
												.findBundles()));
					}
					if (ck.startsWith(BUNDLE_DESCRIPTORS)) {
						@SuppressWarnings("unchecked")
						Collection<BundleDescriptor> bundleDescriptors = (Collection<BundleDescriptor>) configuration
								.get(ck);
						if (bundleDescriptors != null)
							pojoSRDescriptors
									.addAll(convertBundleDescriptors(bundleDescriptors));
					}
				}
				pojoSRConfig.put(PojoServiceRegistryFactory.BUNDLE_DESCRIPTORS,
						pojoSRDescriptors);
			}
			return new ConnectServiceRegistry(new PojoSR(pojoSRConfig));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not create service registry");
		}
	}

}
