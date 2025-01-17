/**
 * This file is part of GeneMANIA.
 * Copyright (C) 2017 University of Toronto.
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
package org.genemania.plugin.cytoscape3.task;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.genemania.plugin.cytoscape3.model.OrganismManager;

public class ListOrganismsCommandTaskFactory extends AbstractTaskFactory {

	private final OrganismManager organismManager;

	public ListOrganismsCommandTaskFactory(OrganismManager organismManager) {
		this.organismManager = organismManager;
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new ListOrganismsCommandTask(organismManager));
	}
}
