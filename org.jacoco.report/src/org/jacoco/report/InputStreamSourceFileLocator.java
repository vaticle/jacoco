/*******************************************************************************
 * Copyright (c) 2009, 2024 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.report;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Abstract base class for {@link ISourceFileLocator} locator implementations
 * based on {@link InputStream}s. It handles the encoding and tab width.
 */
public abstract class InputStreamSourceFileLocator
		implements ISourceFileLocator {

	private final String encoding;
	private final int tabWidth;

	/**
	 * Creates a new locator with the given specification.
	 *
	 * @param encoding
	 *            encoding of the source files, <code>null</code> for platform
	 *            default encoding
	 * @param tabWidth
	 *            tab width in source files as number of blanks
	 *
	 */
	protected InputStreamSourceFileLocator(final String encoding,
			final int tabWidth) {
		this.encoding = encoding;
		this.tabWidth = tabWidth;
	}

	public Reader getSourceFile(final String packageName, final String fileName)
			throws IOException {
		final InputStream in = getSourceStream(packageName.split("/"),
				fileName);

		if (in == null) {
			return null;
		}

		if (encoding == null) {
			return new InputStreamReader(in);
		} else {
			return new InputStreamReader(in, encoding);
		}
	}

	private InputStream getSourceStream(String[] packageName, String fileName)
			throws IOException {
		for (int i = 0; i < packageName.length; i++) {
			final String location = String.join("/",
					Arrays.copyOfRange(packageName, i, packageName.length));
			final InputStream in = getSourceStream(location + "/" + fileName);
			if (in != null) {
				return in;
			}
		}
		return getSourceStream(fileName);
	}

	public int getTabWidth() {
		return tabWidth;
	}

	/**
	 * Tries to locate the given source file and opens its binary content.
	 *
	 * @param path
	 *            local path to the resource
	 * @return stream if the file could be located, <code>null</code> otherwise
	 * @throws IOException
	 *             in case of problems while opening the stream
	 */
	protected abstract InputStream getSourceStream(String path)
			throws IOException;

}
