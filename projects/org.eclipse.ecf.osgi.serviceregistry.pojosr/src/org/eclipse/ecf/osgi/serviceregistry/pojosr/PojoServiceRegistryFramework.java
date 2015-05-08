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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.eclipse.ecf.osgi.serviceregistry.launch.BundleDescriptor;
import org.eclipse.ecf.osgi.serviceregistry.launch.BundleFinder;
import org.eclipse.ecf.osgi.serviceregistry.launch.BundleFinderException;
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

public class PojoServiceRegistryFramework implements Framework {

	private static final int STOP_WAITTIME = 100;

	private final Map<String, String> configuration;
	private final PojoServiceRegistry serviceRegistry;
	private final Collection<BundleFinder> bundleFinder;

	private Bundle frameworkBundle;

	public PojoServiceRegistryFramework(Map<String, String> configuration,
			PojoServiceRegistry serviceRegistry,
			Collection<BundleFinder> bundleFinder) {
		this.configuration = configuration;
		this.serviceRegistry = serviceRegistry;
		this.bundleFinder = bundleFinder;
	}

	protected Map<String, String> getConfiguration() {
		return this.configuration;
	}

	@Override
	public int getState() {
		return (frameworkBundle == null) ? Bundle.INSTALLED : frameworkBundle
				.getState();
	}

	@Override
	public Dictionary<String, String> getHeaders() {
		return frameworkBundle.getHeaders();
	}

	@Override
	public ServiceReference<?>[] getRegisteredServices() {
		return frameworkBundle.getRegisteredServices();
	}

	@Override
	public ServiceReference<?>[] getServicesInUse() {
		return frameworkBundle.getServicesInUse();
	}

	@Override
	public boolean hasPermission(Object permission) {
		return frameworkBundle.hasPermission(permission);
	}

	@Override
	public URL getResource(String name) {
		return frameworkBundle.getResource(name);
	}

	@Override
	public Dictionary<String, String> getHeaders(String locale) {
		return frameworkBundle.getHeaders(locale);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return frameworkBundle.loadClass(name);
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		return frameworkBundle.getResources(name);
	}

	@Override
	public BundleContext getBundleContext() {
		return frameworkBundle.getBundleContext();
	}

	@Override
	public Map<X509Certificate, List<X509Certificate>> getSignerCertificates(
			int signersType) {
		return frameworkBundle.getSignerCertificates(signersType);
	}

	@Override
	public Version getVersion() {
		return frameworkBundle.getVersion();
	}

	@Override
	public File getDataFile(String filename) {
		return frameworkBundle.getDataFile(filename);
	}

	@Override
	public int compareTo(Bundle o) {
		return (o == this) ? 0 : frameworkBundle.compareTo(o);
	}

	@Override
	public void init() throws BundleException {
		init((FrameworkListener[]) null);
	}

	@Override
	public void init(FrameworkListener... listeners) throws BundleException {
		List<BundleDescriptor> bundleDescriptors = new ArrayList<BundleDescriptor>();

		for (BundleFinder bs : this.bundleFinder)
			try {
				bundleDescriptors.addAll(bs.findBundles());
			} catch (BundleFinderException e) {
				throw new BundleException("Could not find bundles", e);
			}

		this.serviceRegistry.initBundles(bundleDescriptors);

		this.frameworkBundle = this.serviceRegistry.getBundleContext()
				.getBundle();
	}

	@Override
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
					lock.wait(STOP_WAITTIME);
				else
					lock.wait();
			}
		}
		return new FrameworkEvent(FrameworkEvent.STOPPED, frameworkBundle, null);
	}

	@Override
	public void start() throws BundleException {
		if (frameworkBundle == null)
			init();
		frameworkBundle.start();
	}

	@Override
	public void start(int options) throws BundleException {
		frameworkBundle.start();
	}

	@Override
	public void stop() throws BundleException {
		frameworkBundle.stop();
	}

	@Override
	public void stop(int options) throws BundleException {
		frameworkBundle.stop(options);
	}

	@Override
	public void uninstall() throws BundleException {
		frameworkBundle.uninstall();
	}

	@Override
	public void update() throws BundleException {
		frameworkBundle.update();
	}

	@Override
	public void update(InputStream in) throws BundleException {
		frameworkBundle.update(in);
	}

	@Override
	public long getBundleId() {
		return frameworkBundle.getBundleId();
	}

	@Override
	public String getLocation() {
		return frameworkBundle.getLocation();
	}

	@Override
	public String getSymbolicName() {
		return frameworkBundle.getSymbolicName();
	}

	@Override
	public Enumeration<String> getEntryPaths(String path) {
		return frameworkBundle.getEntryPaths(path);
	}

	@Override
	public URL getEntry(String path) {
		return frameworkBundle.getEntry(path);
	}

	@Override
	public long getLastModified() {
		return frameworkBundle.getLastModified();
	}

	@Override
	public Enumeration<URL> findEntries(String path, String filePattern,
			boolean recurse) {
		return frameworkBundle.findEntries(path, filePattern, recurse);
	}

	@Override
	public <A> A adapt(Class<A> type) {
		return frameworkBundle.adapt(type);
	}
}
