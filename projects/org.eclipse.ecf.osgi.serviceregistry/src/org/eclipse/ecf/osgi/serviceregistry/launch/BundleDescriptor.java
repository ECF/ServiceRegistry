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

import java.net.URL;
import java.util.Map;

public class BundleDescriptor {

	private final ClassLoader classLoader;
	private final URL url;
	private final Map<String, String> headers;

	public BundleDescriptor(ClassLoader cl, URL url, Map<String, String> headers) {
		this.classLoader = cl;
		this.url = url;
		this.headers = headers;
	}

	public BundleDescriptor(ClassLoader cl, URL url) {
		this(cl, url, null);
	}
	
	public BundleDescriptor(URL url) {
		this(BundleDescriptor.class.getClassLoader(),url);
	}
	
	public ClassLoader getClassLoader() {
		return this.classLoader;
	}

	public URL getURL() {
		return this.url;
	}

	public Map<String, String> getHeaders() {
		return this.headers;
	}

	@Override
	public String toString() {
		return "BundleDescriptor [classLoader=" + classLoader + ", url=" + url
				+ ", headers=" + headers + "]";
	}

}
