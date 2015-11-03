/*******************************************************************************
 * Copyright (c) 2015 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.osgi.serviceregistry.launch;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JarFileBundleFinder implements BundleFinder {

	public static final String JAR_EXTENSION = ".jar";
	private Path startPath = Paths.get("");
	private Set<FileVisitOption> options = EnumSet.noneOf(FileVisitOption.class);
	private int maxDepth = Integer.MAX_VALUE;
	private Set<String> extensions = new HashSet<String>();
	
	public static class Builder {
		
		JarFileBundleFinder f;
		
		public Builder() {
			f = new JarFileBundleFinder();
			f.extensions.add(JAR_EXTENSION);
		}
		
		public JarFileBundleFinder setStartPath(Path path) {
			f.startPath = path;
			return f;
		}
		
		public JarFileBundleFinder setFileVisitOptions(Set<FileVisitOption> opts) {
			f.options = opts;
			return f;
		}
		
		public JarFileBundleFinder setMaxDepth(int mDepth) {
			f.maxDepth = mDepth;
			return f;
		}
		
		public JarFileBundleFinder addExtension(String ext) {
			f.extensions.add(ext);
			return f;
		}
		
		public JarFileBundleFinder removeExtension(String ext) {
			f.extensions.remove(ext);
			return f;
		}
	}
	
	@Override
	public Collection<BundleDescriptor> findBundles() throws BundleFinderException {
		final List<BundleDescriptor> results = new ArrayList<BundleDescriptor>();
		try {
			Files.walkFileTree(startPath, options, maxDepth, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					String fileName = file.getFileName().toString();
					for (String s : extensions) 
						if (fileName.endsWith(s)) 
							results.add(new BundleDescriptor(file.toUri().toURL()));
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			throw new BundleFinderException("Cannot walk file tree starting at "+startPath);
		}
		return results;
	}

}
