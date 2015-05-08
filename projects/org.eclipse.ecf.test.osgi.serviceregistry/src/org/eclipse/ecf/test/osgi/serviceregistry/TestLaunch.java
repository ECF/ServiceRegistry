/*******************************************************************************
 * Copyright (c) 2015 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.test.osgi.serviceregistry;

import java.util.HashMap;
import java.util.ServiceLoader;

import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class TestLaunch {

	public static void main(String[] args) throws Exception {
		FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class).iterator().next();
		System.out.println("frameworkFactory="+frameworkFactory);
		Framework framework = frameworkFactory.newFramework(new HashMap<String,String>());
		framework.start();
	}

}
