/**
 * This file is part of GeneMANIA.
 * Copyright (C) 2010 University of Toronto.
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

/**
 * InteractionNetworkGroupMock: InteractionNetworkGroup mock object
 * Created Jul 28, 2009
 * @author Ovi Comes
 */
package org.genemania.mock;

import java.util.ArrayList;
import java.util.Collection;

import org.genemania.domain.InteractionNetwork;
import org.genemania.domain.InteractionNetworkGroup;

public class InteractionNetworkGroupMock {
	
	public static InteractionNetworkGroup getMockObject(long id) {
		InteractionNetworkGroup ret = new InteractionNetworkGroup();
		ret.setDescription("just a mock interaction network group");
		ret.setId(id);
		Collection<InteractionNetwork> interactionNetworks = new ArrayList<InteractionNetwork>();
		interactionNetworks.add(InteractionNetworkMock.getMockObject(id));
		ret.setInteractionNetworks(interactionNetworks);
		ret.setName("mock interaction network group " + id);
		return ret;
	}
	
}
