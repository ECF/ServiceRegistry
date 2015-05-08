/*
 * Copyright 2011 Karl Pauls karlpauls@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Modifications in 2015 by Scott Lewis
 */
package org.eclipse.ecf.osgi.serviceregistry.launch;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;

public class ClasspathBundleFinder implements BundleFinder {

	private String filter;
	private ClassLoader classLoader;

	public ClasspathBundleFinder(String filter, ClassLoader classLoader) {
		this.filter = filter;
		this.classLoader = classLoader;
	}

	public ClasspathBundleFinder(String filter) {
		this(filter, null);
	}

	public ClasspathBundleFinder(ClassLoader classLoader) {
		this(null, classLoader);
	}

	public ClasspathBundleFinder() {
		this(null, null);
	}

	@Override
	public Collection<BundleDescriptor> findBundles()
			throws BundleFinderException {
		Filter filter;
		try {
			filter = (this.filter != null) ? FrameworkUtil
					.createFilter(this.filter) : null;
		} catch (InvalidSyntaxException e1) {
			throw new BundleFinderException("Could not create filter", e1);
		}

		ClassLoader loader = (classLoader != null) ? classLoader : getClass()
				.getClassLoader();

		List<BundleDescriptor> bundles = new ArrayList<BundleDescriptor>();
		byte[] bytes = new byte[1024 * 1024 * 2];
		try {
			for (Enumeration<URL> e = loader
					.getResources("META-INF/MANIFEST.MF"); e.hasMoreElements();) {
				URL manifestURL = e.nextElement();
				InputStream input = null;
				try {
					input = manifestURL.openStream();
					int size = 0;
					for (int i = input.read(bytes); i != -1; i = input.read(
							bytes, size, bytes.length - size)) {
						size += i;
						if (size == bytes.length) {
							byte[] tmp = new byte[size * 2];
							System.arraycopy(bytes, 0, tmp, 0, bytes.length);
							bytes = tmp;
						}
					}
					String key = null;
					int last = 0;
					int current = 0;

					Map<String, String> headers = new HashMap<String, String>();
					for (int i = 0; i < size; i++) {
						if (bytes[i] == '\r') {
							if ((i + 1 < size) && (bytes[i + 1] == '\n')) {
								continue;
							}
						}
						if (bytes[i] == '\n') {
							if ((i + 1 < size) && (bytes[i + 1] == ' ')) {
								i++;
								continue;
							}
						}
						if ((key == null) && (bytes[i] == ':')) {
							key = new String(bytes, last, (current - last),
									"UTF-8");
							if ((i + 1 < size) && (bytes[i + 1] == ' ')) {
								last = current + 1;
								continue;
							} else {
								throw new BundleFinderException(
										"Manifest error: Missing space separator - "
												+ key);
							}
						}
						if (bytes[i] == '\n') {
							if ((last == current) && (key == null)) {
								break;
							}
							String value = new String(bytes, last,
									(current - last), "UTF-8");
							if (key == null) {
								throw new BundleFinderException(
										"Manifst error: Missing attribute name - "
												+ value);
							} else if (headers.put(key, value) != null) {
								throw new BundleFinderException(
										"Manifst error: Duplicate attribute name - "
												+ key);
							}
							last = current;
							key = null;
						} else {
							bytes[current++] = bytes[i];
						}
					}
					if ((filter == null)
							|| filter.match(convertMapToDictionary(headers))) {
						bundles.add(new BundleDescriptor(loader,
								getParentURL(manifestURL), headers));
					}
				} finally {
					if (input != null)
						input.close();
				}
			}
		} catch (IOException e) {
			throw new BundleFinderException("Could not read from jar", e);
		}
		return bundles;
	}

	private Dictionary<String, String> convertMapToDictionary(
			Map<String, String> map) {
		Hashtable<String, String> results = new Hashtable<String, String>();
		for (String key : map.keySet())
			results.put(key, map.get(key));
		return results;
	}

	private URL getParentURL(URL url) throws MalformedURLException {
		String externalForm = url.toExternalForm();
		return new URL(externalForm.substring(0, externalForm.length() - 20));
	}

}
