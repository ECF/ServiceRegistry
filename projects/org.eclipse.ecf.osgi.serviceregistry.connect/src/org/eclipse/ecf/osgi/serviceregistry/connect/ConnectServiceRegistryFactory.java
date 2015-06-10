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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;
import org.apache.felix.connect.PojoSR;
import org.apache.felix.connect.launch.ClasspathScanner;
import org.apache.felix.connect.launch.PojoServiceRegistry;

public class ConnectServiceRegistryFactory implements ServiceRegistryFactory {

    public static final String BUNDLE_DESCRIPTORS =
            PojoServiceRegistry.class.getName().toLowerCase() + ".bundles";

	public ServiceRegistry newServiceRegistry(
			final Map<String, ?> configuration) {
		try {
			Map<String,Object> config = new HashMap<String,Object>();
			Object bs = null;
			if (configuration != null) {
				config.putAll(configuration);
				bs = configuration.get(BUNDLE_DESCRIPTORS);
			}

			if (bs == null) 
				config.put(BUNDLE_DESCRIPTORS, new ClasspathScanner().scanForBundles());

			return new ConnectServiceRegistry(new PojoSR(config));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
