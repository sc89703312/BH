package edu.nju.controller.impl;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.controller.msgqueue.operation.SetupHostOperation;
import edu.nju.controller.service.HostControllerService;

public class HostControllerImpl implements HostControllerService{

	@Override
	public boolean serviceetupHost() {
		// TODO Auto-generated method stub
		OperationQueue.addMineOperation(new SetupHostOperation());
		return false;
	}

}
