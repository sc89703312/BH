package edu.nju.controller.impl;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.controller.msgqueue.operation.SetupClientOperation;
import edu.nju.controller.service.ClientControllerService;

public class ClientControllerImpl implements ClientControllerService{

	@Override
	public boolean setupClient(String ip) {
		// TODO Auto-generated method stub
		OperationQueue.addMineOperation(new SetupClientOperation());
		return false;
	}

}
