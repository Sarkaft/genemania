/**
 * This file is part of GeneMANIA.
 * Copyright (C) 2008-2011 University of Toronto.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.genemania.plugin.cytoscape3.delegates;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.cytoscape.model.CyNetwork;
import org.genemania.exception.ApplicationException;
import org.genemania.exception.DataStoreException;
import org.genemania.plugin.GeneMania;
import org.genemania.plugin.cytoscape.CytoscapeUtils;
import org.genemania.plugin.cytoscape.ResultReconstructor;
import org.genemania.plugin.data.DataSet;
import org.genemania.plugin.data.DataSetManager;
import org.genemania.plugin.delegates.Delegate;
import org.genemania.plugin.model.ViewState;
import org.genemania.plugin.selection.SessionManager;
import org.genemania.util.ProgressReporter;

public class SessionChangeDelegate implements Delegate {
	
	private final File dataSetPath;
	private final GeneMania plugin;
	private final ProgressReporter progress;
	private final CytoscapeUtils cytoscapeUtils;

	public SessionChangeDelegate(
			File dataSetPath,
			GeneMania plugin,
			ProgressReporter progress,
			CytoscapeUtils cytoscapeUtils
	) {
		this.dataSetPath = dataSetPath;
		this.plugin = plugin;
		this.progress = progress;
		this.cytoscapeUtils = cytoscapeUtils;
	}
	
	@Override
	public void invoke() throws ApplicationException {
		DataSetManager dataSetManager = plugin.getDataSetManager();
		dataSetManager.setDataSourcePath(dataSetPath);
		
		try {
			DataSet data = findDataSet(dataSetPath, dataSetManager);
			
			// When the session only has genemania networks from online searches, dataSetPath is null
			if (data == null && dataSetPath != null) {
				plugin.initializeData(progress, true);
				data = dataSetManager.getDataSet();
				
				if (data == null)
					throw new ApplicationException("Cannot load GeneMANIA data. Please download a data set and open this session again");
			}
			
			SessionManager manager = plugin.getSessionManager();
			ResultReconstructor reconstructor = new ResultReconstructor(data, plugin, cytoscapeUtils);
			
			// Reconstruct networks - Web Data
			Map<CyNetwork, ViewState> states = cytoscapeUtils.restoreSessionState(progress);
			states.forEach((net, options) -> manager.addNetworkConfiguration(net, options));
			
			// Reconstruct networks - Local Data
			for (CyNetwork cyNetwork : cytoscapeUtils.getNetworks()) {
				ViewState options = reconstructor.reconstructCache(cyNetwork, progress);
				
				if (options == null)
					continue;
				
				manager.addNetworkConfiguration(cyNetwork, options);
			}
		} catch (IOException e) {
			throw new ApplicationException(e);
		} catch (DataStoreException e) {
			throw new ApplicationException(e);
		}
	}

	private DataSet findDataSet(File path, DataSetManager dataSetManager) throws ApplicationException {
		if (path == null)
			return null;
		
		if (path.exists()) {
			plugin.loadDataSet(path, progress, false, true);
			return dataSetManager.getDataSet();
		}
		
		String rawPath = path.getPath();
		
		for (String delimiter : new String[] { "/", "\\\\" }) { //$NON-NLS-1$ //$NON-NLS-2$
			String[] parts = rawPath.split(delimiter);
			String name = parts[parts.length - 1];
			File candidatePath = dataSetManager.getDataSetPath(name);
			
			if (candidatePath == null)
				continue;
			
			plugin.loadDataSet(candidatePath, progress, false, true);
			return dataSetManager.getDataSet();
		}
		
		return null;
	}
}
