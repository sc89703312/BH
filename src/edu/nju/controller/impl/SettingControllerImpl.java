package edu.nju.controller.impl;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.controller.msgqueue.operation.*;
import edu.nju.controller.service.SettingControllerService;

public class SettingControllerImpl implements SettingControllerService{

	@Override
	public boolean setEasyGameLevel() {
		// TODO Auto-generated method stub
		OperationQueue.addMineOperation(new SetEasyOperation());
		return true;
	}

	@Override
	public boolean setHardGameLevel() {
		// TODO Auto-generated method stub
		OperationQueue.addMineOperation(new SetHardOperation());
		return true;
	}

	@Override
	public boolean setHellGameLevel() {
		// TODO Auto-generated method stub
		OperationQueue.addMineOperation(new SetHellOperation());
		return true;
	}

	@Override
	public boolean setCustomizedGameLevel(int height, int width, int nums) {
		// TODO Auto-generated method stub
		OperationQueue.addMineOperation(new SetCustomOperation(height,width,nums));
		return true;
	}

}
