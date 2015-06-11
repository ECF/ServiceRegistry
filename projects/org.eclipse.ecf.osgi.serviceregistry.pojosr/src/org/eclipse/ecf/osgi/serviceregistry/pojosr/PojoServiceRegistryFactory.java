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

import java.util.Map;

import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistry;
import org.eclipse.ecf.osgi.serviceregistry.ServiceRegistryFactory;

import de.kalpatec.pojosr.framework.PojoSR;

public class PojoServiceRegistryFactory implements ServiceRegistryFactory {

	@Override
	public ServiceRegistry newServiceRegistry(final Map<String, ?> configuration) {
		try {
			return new PojoServiceRegistry(new PojoSR(configuration));
		} catch (Exception e) {
			return null;
		}
	}

}
