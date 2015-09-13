package edu.nju.controller.msgqueue.operation;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.model.impl.ChessBoardModelImpl;
import edu.nju.model.impl.GameModelImpl;
import edu.nju.model.impl.ParameterModelImpl;
import edu.nju.network.Configure;
import edu.nju.network.client.ClientInHandlerImpl;
import edu.nju.network.client.ClientServiceImpl;

public class SetupClientOperation extends MineOperation{

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		GameModelImpl game = (GameModelImpl) OperationQueue.getGameModel();
		ChessBoardModelImpl board = (ChessBoardModelImpl) OperationQueue.getChessBoardModel();
		ParameterModelImpl parameter = (ParameterModelImpl) board.getParameterModel();
		
		ClientServiceImpl client = new ClientServiceImpl();
		ClientInHandlerImpl clientH = new ClientInHandlerImpl();
		
        clientH.addObserver(game);
        clientH.addObserver(board);
        clientH.addObserver(parameter);
        
        client.init(Configure.SERVER_ADDRESS,clientH);
        
        ChessBoardModelImpl.isOnNet = true;
        OperationQueue.setClient();
        OperationQueue.connectToNet(client);
		
	}

}
