package edu.nju.model.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.nju.model.service.ChessBoardModelService;
import edu.nju.model.service.GameModelService;
import edu.nju.model.service.StatisticModelService;
import edu.nju.model.state.GameResultState;
import edu.nju.model.state.GameState;
import edu.nju.model.vo.GameVO;

public class GameModelImpl extends BaseModel implements GameModelService{
	
	private StatisticModelService statisticModel;
	private ChessBoardModelService chessBoardModel;
	
	private List<GameLevel> levelList;
	
	private GameState gameState;
	private int width;
	private int height;
	private int mineNum;
	private String level;
	
	private GameResultState gameResultState;
	private int time;
	
	private long startTime;
	
    static String currentLevel;
    private static boolean firstGame = true;
    Timer timer;

	public GameModelImpl(StatisticModelService statisticModel, ChessBoardModelService chessBoardModel){
		this.statisticModel = statisticModel;
		this.chessBoardModel = chessBoardModel;
		gameState = GameState.OVER;
		
		chessBoardModel.setGameModel(this);
		
		levelList = new ArrayList<GameLevel>();
		levelList.add(new GameLevel(0,"大",30,16,99));
		levelList.add(new GameLevel(1,"中",16,16,40));
		levelList.add(new GameLevel(2,"小",9,9,10));
		
		level = "小";
		
	}

	@Override
	public boolean startGame() {
		// TODO Auto-generated method stub
		gameState = GameState.RUN;
		startTime = Calendar.getInstance().getTimeInMillis();
		
		GameLevel gl = null;
		for(GameLevel tempLevel : levelList){
			if(tempLevel.getName().equals(level)){
				gl = tempLevel;
				break;
			}
		}
		
		if(level.equals("自定义")){
			gl = null;
		}
		
		if(gl == null&&width==0&&height == 0)
			gl = levelList.get(2);

		
		if(gl != null){
			width = gl.getWidth();
			height = gl.getHeight();
			mineNum = gl.getMineNum();
		}
		
		this.chessBoardModel.initialize(width, height, mineNum);
		System.out.println(mineNum);
		
		if(timer!=null)
			timer.cancel();
		timer = new Timer();
		timer.schedule(new TimerTask(){
			
			int secondCount = 0;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				secondCount++;
				updateChange(new UpdateMessage("timeplus",secondCount));
			}
			
		}, 0, 1000);
		
		ChessBoardModelImpl.clientMarkNum = 0;
		ChessBoardModelImpl.hostMarkNum = 0;
		ChessBoardModelImpl.isFirstClick = true;
		
		super.updateChange(new UpdateMessage("markcountchange", ChessBoardModelImpl.hostMarkNum+";"+ChessBoardModelImpl.clientMarkNum));
		super.updateChange(new UpdateMessage("start",this.convertToDisplayGame()));

		return true;
	}
	

	@Override
	public boolean gameOver(GameResultState result) {
		// TODO Auto-generated method stub
		
		System.out.println("gameover");
		this.gameState = GameState.OVER;
		this.gameResultState = result;
		this.time = (int)(Calendar.getInstance().getTimeInMillis() - startTime)/1000;
		
		this.statisticModel.recordStatistic(gameResultState, time);
		
		timer.cancel();
		
		if(result==GameResultState.SUCCESS){
			if(ChessBoardModelImpl.isOnNet){
				int hostNum = ChessBoardModelImpl.hostMarkNum;
				int clientNum = ChessBoardModelImpl.clientMarkNum;
				if(hostNum>clientNum)
					super.updateChange(new UpdateMessage("hostwin",this.convertToDisplayGame()));
				else
					super.updateChange(new UpdateMessage("clientwin",this.convertToDisplayGame()));
			}
			else
		        super.updateChange(new UpdateMessage("success",this.convertToDisplayGame()));		
		}
		else
			if(result==GameResultState.FAIL)
				super.updateChange(new UpdateMessage("fail",this.convertToDisplayGame()));
			else
				if(result==GameResultState.HOSTWIN)
					super.updateChange(new UpdateMessage("hostwin",this.convertToDisplayGame()));
				else
					if(result==GameResultState.CLIENTWIN)
						super.updateChange(new UpdateMessage("clientwin",this.convertToDisplayGame()));
		
		return true;
	}

	@Override
	public boolean setGameLevel(String level) {
		// TODO Auto-generated method stub
		//输入校验
		this.level = level;
		currentLevel = level;
		return true;
	}

	@Override
	public boolean setGameSize(int width, int height, int mineNum) {
		// TODO Auto-generated method stub
		//输入校验
		this.width = width;
		this.height = height;
		this.mineNum = mineNum;
		return true;
	}
	
	private GameVO convertToDisplayGame(){
		return new GameVO(gameState, width, height,level, gameResultState, time);
	}

	@Override
	public List<GameLevel> getGameLevel() {
		// TODO Auto-generated method stub
		return this.levelList;
	}
	
	public static void handleFirstGame(){
		firstGame = false;
	}
	
	public static boolean isFirstGame(){
		return firstGame;
	}
}