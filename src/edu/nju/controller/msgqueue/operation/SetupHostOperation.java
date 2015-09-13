package edu.nju.controller.msgqueue.operation;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.model.impl.ChessBoardModelImpl;
import edu.nju.model.impl.GameModelImpl;
import edu.nju.model.impl.ParameterModelImpl;
import edu.nju.network.host.HostInHandlerImpl;
import edu.nju.network.host.HostServiceImpl;

public class SetupHostOperation extends MineOperation{

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		GameModelImpl game = (GameModelImpl) OperationQueue.getGameModel();
		ChessBoardModelImpl board = (ChessBoardModelImpl) OperationQueue.getChessBoardModel();
		ParameterModelImpl parameter = (ParameterModelImpl)board.getParameterModel();
		
		HostServiceImpl host = new HostServiceImpl();
		HostInHandlerImpl hostH = new HostInHandlerImpl();
		
		game.addObserver(host);
		board.addObserver(host);
		parameter.addObserver(host);
		if(host.init(hostH)){
			System.out.println("Connecting!!!");
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			ChessBoardModelImpl.isOnNet = true;
			game.startGame();
		}
		
	}

}
