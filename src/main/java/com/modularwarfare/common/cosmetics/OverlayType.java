package com.modularwarfare.common.cosmetics;

import com.modularwarfare.common.type.BaseType;

public class OverlayType extends BaseType {

	public String frameTooltip = null;
	public int usableMaxAmount = 5;

	@Override
	public void loadExtraValues() {
		if(maxStackSize == null)
			maxStackSize = 1;
		if (frameTooltip == null)
			frameTooltip = "";

		loadBaseValues();

	}
	
	@Override
	public void reloadModel()
	{
	}
	
	@Override
	public String getAssetDir()
	{
		return "overlay";
	}

}
