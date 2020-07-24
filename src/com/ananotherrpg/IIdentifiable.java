package com.ananotherrpg;

/**
 *  Any object that can be identified by a player
 */
public interface IIdentifiable extends IQueryable{
	
	public int getID();
	
	public String getDescription();

	public String getDetailForm();

	
}
