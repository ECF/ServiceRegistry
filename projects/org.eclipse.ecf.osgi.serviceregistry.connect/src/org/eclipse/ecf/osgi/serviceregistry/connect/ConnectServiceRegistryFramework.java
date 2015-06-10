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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.framework.Version;
import org.osgi.framework.launch.Framework;

public class ConnectServiceRegistryFramework implements Framework {

	private static final int STOP_WAITTIME = 100;

	private final ConnectServiceRegistry serviceRegistry;
	private final int stopWaitTime;
	private Bundle frameworkBundle;

	public ConnectServiceRegistryFramework(
			ConnectServiceRegistry serviceRegistry) {
		this(serviceRegistry, STOP_WAITTIME);
	}

	public ConnectServiceRegistryFramework(
			ConnectServiceRegistry serviceRegistry, int stopWaitTime) {
		this.serviceRegistry = serviceRegistry;
		this.stopWaitTime = stopWaitTime;
	}

	public int getState() {
		return (frameworkBundle == null) ? Bundle.INSTALLED : frameworkBundle
				.getState();
	}

	public Dictionary<String, String> getHeaders() {
		return frameworkBundle.getHeaders();
	}

	public ServiceReference<?>[] getRegisteredServices() {
		return frameworkBundle.getRegisteredServices();
	}

	public ServiceReference<?>[] getServicesInUse() {
		return frameworkBundle.getServicesInUse();
	}

	public boolean hasPermission(Object permission) {
		return frameworkBundle.hasPermission(permission);
	}

	public URL getResource(String name) {
		return frameworkBundle.getResource(name);
	}

	public Dictionary<String, String> getHeaders(String locale) {
		return frameworkBundle.getHeaders(locale);
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return frameworkBundle.loadClass(name);
	}

	public Enumeration<URL> getResources(String name) throws IOException {
		return frameworkBundle.getResources(name);
	}

	public BundleContext getBundleContext() {
		return frameworkBundle.getBundleContext();
	}

	public Map<X509Certificate, List<X509Certificate>> getSignerCertificates(
			int signersType) {
		return frameworkBundle.getSignerCertificates(signersType);
	}

	public Version getVersion() {
		return frameworkBundle.getVersion();
	}

	public File getDataFile(String filename) {
		return frameworkBundle.getDataFile(filename);
	}

	public int compareTo(Bundle o) {
		return (o == this) ? 0 : frameworkBundle.compareTo(o);
	}

	public void init() throws BundleException {
		init((FrameworkListener[]) null);
	}

	public void init(FrameworkListener... listeners) throws BundleException {
		this.frameworkBundle = this.serviceRegistry.getBundleContext()
				.getBundle();
	}

	public FrameworkEvent waitForStop(long timeout) throws InterruptedException {
		final Object lock = new Object();
		frameworkBundle.getBundleContext().addBundleListener(
				new SynchronousBundleListener() {
					public void bundleChanged(BundleEvent event) {
						if ((event.getBundle() == frameworkBundle)
								&& (event.getType() == BundleEvent.STOPPED)) {
							synchronized (lock) {
								lock.notifyAll();
							}
						}
					}
				});
		synchronized (lock) {
			while (frameworkBundle.getState() != Bundle.RESOLVED) {
				if (frameworkBundle.getState() == Bundle.STOPPING)
					lock.wait(stopWaitTime);
				else
					lock.wait();
			}
		}
		return new FrameworkEvent(FrameworkEvent.STOPPED, frameworkBundle, null);
	}

	public void start() throws BundleException {
		if (frameworkBundle == null)
			init();
		frameworkBundle.start();
	}

	public void start(int options) throws BundleException {
		frameworkBundle.start();
	}

	public void stop() throws BundleException {
		frameworkBundle.stop();
	}

	public void stop(int options) throws BundleException {
		frameworkBundle.stop(options);
	}

	public void uninstall() throws BundleException {
		frameworkBundle.uninstall();
	}

	public void update() throws BundleException {
		frameworkBundle.update();
	}

	public void update(InputStream in) throws BundleException {
		frameworkBundle.update(in);
	}

	public long getBundleId() {
		return frameworkBundle.getBundleId();
	}

	public String getLocation() {
		return frameworkBundle.getLocation();
	}

	public String getSymbolicName() {
		return frameworkBundle.getSymbolicName();
	}

	public Enumeration<String> getEntryPaths(String path) {
		return frameworkBundle.getEntryPaths(path);
	}

	public URL getEntry(String path) {
		return frameworkBundle.getEntry(path);
	}

	public long getLastModified() {
		return frameworkBundle.getLastModified();
	}

	public Enumeration<URL> findEntries(String path, String filePattern,
			boolean recurse) {
		return frameworkBundle.findEntries(path, filePattern, recurse);
	}

	public <A> A adapt(Class<A> type) {
		return frameworkBundle.adapt(type);
	}
}
