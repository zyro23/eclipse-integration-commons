/*******************************************************************************
 *  Copyright (c) 2012 VMware, Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *      VMware, Inc. - initial API and implementation
 *******************************************************************************/
package org.springsource.ide.eclipse.commons.content.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;
import org.springsource.ide.eclipse.commons.core.StatusHandler;


/**
 * @author Terry Denney
 * @author Steffen Pingel
 */
public class ContentPlugin extends Plugin {

	/**
	 * The file that contains default descriptors located in the bundle root.
	 */
	private static final String FILENAME_DESCRIPTORS = "descriptors.xml";

	/**
	 * The file in the bundle state directory that has information about
	 * installed and available content.
	 */
	private static final String FILENAME_CONTENT = "content.xml";

	public static final String PLUGIN_ID = "org.springsource.ide.eclipse.commons.content.core";

	private static ContentPlugin plugin;

	public static ContentPlugin getDefault() {
		return plugin;
	}

	private ContentManager manager;

	public ContentPlugin() {
	}

	public ContentManager getManager() {
		init();
		return manager;
	}

	private synchronized void init() {
		if (manager != null) {
			return;
		}

		manager = new ContentManager();
		File file = new File(Platform.getStateLocation(getBundle()).toFile(), FILENAME_CONTENT);
		manager.setStateFile(file);
		try {
			URL entry = getBundle().getEntry(FILENAME_DESCRIPTORS);
			if (entry != null) {
				URL localURL = FileLocator.toFileURL(entry);
				manager.setDefaultStateFile(new File(localURL.getFile()));
			}
		}
		catch (IOException e) {
			StatusHandler.log(new Status(IStatus.WARNING, PLUGIN_ID, "Unable to locate dfault descriptor for content",
					e));
		}
		manager.init();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

}
