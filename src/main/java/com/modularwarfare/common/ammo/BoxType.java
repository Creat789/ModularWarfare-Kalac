package com.modularwarfare.common.ammo;

import com.modularwarfare.common.type.BaseType;

public class BoxType extends BaseType {

	public String skinName;

	@Override
	public void loadExtraValues()
	{
		if(maxStackSize == null)
			maxStackSize = 1;
		
		loadBaseValues();
	}
	
	@Override
	public void reloadModel()
	{
	}
	
	@Override
	public String getAssetDir()
	{
		return "boxes";
	}

}
