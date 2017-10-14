package invmod.entity;

import invmod.tileentity.TileEntityNexus;


public interface IHasNexus
{
	public TileEntityNexus getNexus();

	public void acquiredByNexus(TileEntityNexus nexus);
}