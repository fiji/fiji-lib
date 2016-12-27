/*-
 * #%L
 * Fiji distribution of ImageJ for the life sciences.
 * %%
 * Copyright (C) 2009 - 2016 Fiji development team.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package fiji;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * This class helps you with accessing libraries using JNI
 */
public class JNI {
	public static final String platform;
	public static final String libraryDirectory;
	public static final String libraryPrefix;
	public static final String libraryExtension;
	public static final String fallbackLibraryExtension;

	protected static Set<String> loadedLibraries = new HashSet<String>();

	static {
		String os = System.getProperty("os.name");
		boolean is64bit = System.getProperty("os.arch", "").indexOf("64") >= 0;
		String osName = System.getProperty("os.name", "<unknown>");
		if (osName.equals("Linux")) {
			platform = "linux" + (is64bit ? "64" : "32");
			libraryPrefix = "lib";
			libraryExtension = ".so";
			fallbackLibraryExtension = null;
		}
		else if (osName.equals("Mac OS X")) {
			platform = "macosx";
			libraryPrefix = "lib";
			libraryExtension = ".dylib";
			fallbackLibraryExtension = ".jnilib";
		}
		else if (osName.startsWith("Windows")) {
			platform = "win" + (is64bit ? "64" : "32");
			libraryPrefix = "";
			libraryExtension = ".dll";
			fallbackLibraryExtension = null;
		}
		else {
			String arch = is64bit ? "64" : "32";
			platform = osName.toLowerCase() + (osName.endsWith(arch) ? "" : arch);
			libraryPrefix = "lib";
			libraryExtension = ".so";
			fallbackLibraryExtension = null;
		}

		libraryDirectory = System.getProperty("ij.dir") + "/lib/"
			+ (platform != null ? platform + "/" : "");
	}

	public static void loadLibraries(String nameWithWildcards) {
		Pattern regex = glob2regex(libraryPrefix + nameWithWildcards + libraryExtension);
		File dir = new File(libraryDirectory);
		for (File file : dir.listFiles())
			if (regex.matcher(file.getName()).matches())
				loadLibrary(file);
	}

	public static void loadLibrary(String name) {
		File library = new File(name);
		if (!library.isAbsolute()) {
			library = new File(libraryDirectory, libraryPrefix + name + libraryExtension);
			if (fallbackLibraryExtension != null && !library.exists())
				library = new File(libraryDirectory, libraryPrefix + name + fallbackLibraryExtension);
		}
		loadLibrary(library);
	}

	public static void loadLibrary(File library) {
		if (loadedLibraries.contains(library.getAbsolutePath()))
			return;
		if (library.exists())
			System.load(library.getAbsolutePath());
		else
			System.loadLibrary(library.getName());
		loadedLibraries.add(library.getAbsolutePath());
	}

	public static Pattern glob2regex(String glob) {
		StringBuffer result = new StringBuffer();
		result.append("^");

		char[] array = glob.toCharArray();
		int len = array.length;
		for (int i = 0; i < len; i++) {
			char c = array[i];
			if (".^$".indexOf(c) >= 0)
				result.append("\\" + c);
			else if (c == '?')
				result.append("[^/]");
			else if (c == '*') {
				if (i + 1 >= len || array[i + 1] != '*')
					result.append("[^/]*");
				else {
					result.append(".*");
					i++;
					if (i + 1 < len && array[i + 1]
							== '/')
						i++;
				}
			} else
				result.append(c);
		}

		result.append("$");
		return Pattern.compile(result.toString());
	}
}
