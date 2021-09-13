/*-
 * #%L
 * Fiji distribution of ImageJ for the life sciences.
 * %%
 * Copyright (C) 2009 - 2021 Fiji developers.
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

import fiji.debugging.Object_Inspector;
import ij.plugin.PlugIn;
import ij.plugin.filter.PlugInFilter;

import java.io.File;

import net.imagej.patcher.LegacyEnvironment;
import net.imagej.patcher.LegacyInjector;

public class Debug {
	static {
		LegacyInjector.preinit();
	}

	public static void show(Object object) {
		Object_Inspector.openFrame("" + object, object);
	}

	public static Object get(Object object, String fieldName) {
		return Object_Inspector.get(object, fieldName);
	}

	/**
	 * Debug helper
	 *
	 * Call this function from your debugger to debug your plugin
	 *
	 * @param plugin the menu item label of the plugin to run
	 * @param parameters the parameters as recorded by the Recorder, or null if dialogs should pop up
	 */
	public static void run(String plugin, String parameters) {
		runFilter(null, plugin, parameters);
	}

	/**
	 * Debug helper
	 *
	 * Call this function from your debugger to debug your filter plugin
	 *
	 * @param imagePath the path to the example image to test with
	 * @param plugin the menu item label of the plugin to run
	 * @param parameters the parameters as recorded by the Recorder, or null if dialogs should pop up
	 */
	public static void runFilter(String imagePath, String plugin, String parameters) {
		runFilter(imagePath, plugin, parameters, false);
	}

	/**
	 * Debug helper
	 *
	 * Call this function from your debugger to debug your filter plugin
	 *
	 * @param imagePath the path to the example image to test with
	 * @param plugin the menu item label of the plugin to run
	 * @param parameters the parameters as recorded by the Recorder, or null if dialogs should pop up
	 * @param headless whether to run in headless mode
	 */
	public static void runFilter(String imagePath, String plugin, String parameters, final boolean headless) {
		try {
			System.setProperty("ij1.plugin.dirs", "/non-existing/");
			final LegacyEnvironment ij1 = new LegacyEnvironment(null, headless);
			ij1.addPluginClasspath(Thread.currentThread().getContextClassLoader());
			// show UI
			if (!headless) ij1.main();
			if (imagePath != null) {
				final File file = new File(imagePath);
				if (!file.isAbsolute()) {
					imagePath = file.getAbsolutePath();
				}
				ij1.runMacro("open('" + imagePath + "');", "");
			}
			ij1.run(plugin, parameters);
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Debug helper
	 *
	 * Call this function from your debugger to debug your filter plugin
	 *
	 * @param className the name of the class implementing either {@link PlugIn} or {@link PlugInFilter}
	 * @param arg the {@link String} argument passed to the plugin's {@code run} or {@code setup} method
	 * @param headless whether to run in headless mode
	 * @return the plugin instance (whose class was loaded in a different class loader than the current one!)
	 */
	public static Object runPlugIn(final String className, final String arg, final boolean headless) {
		try {
			System.setProperty("ij1.plugin.dirs", "/non-existing/");
			final LegacyEnvironment ij1 = new LegacyEnvironment(null, headless);
			ij1.addPluginClasspath(Thread.currentThread().getContextClassLoader());
			// show UI
			if (!headless) ij1.main();
			return ij1.runPlugIn(className, arg);
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
