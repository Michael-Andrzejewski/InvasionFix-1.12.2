package invmod.entity;

import invmod.entity.ai.navigator.Path;


public abstract interface IPathResult
{
	public abstract void pathCompleted(Path paramPath);
}