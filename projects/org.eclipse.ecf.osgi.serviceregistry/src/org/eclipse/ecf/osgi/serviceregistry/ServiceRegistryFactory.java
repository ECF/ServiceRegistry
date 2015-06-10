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

import java.util.Map;

public interface ServiceRegistryFactory {

	public static final String BUNDLE_DESCRIPTORS = "org.eclipse.ecf.osgi.serviceregistry.bundledescriptors";
	public static final String BUNDLE_FINDERS = "org.eclipse.ecf.osgi.serviceregistry.bundlefinders";

	ServiceRegistry newServiceRegistry(Map<String, ?> configuration);
}
