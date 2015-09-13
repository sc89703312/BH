package edu.nju.controller.msgqueue.operation;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.model.service.GameModelService;

public class SetCustomOperation extends MineOperation{
	
	int height;
	int width;
	int mineNum;
	
	public SetCustomOperation(int height,int width,int mineNum){
		this.height = height;
		this.width = width;
		this.mineNum = mineNum;
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		GameModelService game = OperationQueue.getGameModel();
		
		System.out.println("ok!");
		
		game.setGameSize(width,height,mineNum);
		game.setGameLevel("自定义");
		game.startGame();
	}

}
