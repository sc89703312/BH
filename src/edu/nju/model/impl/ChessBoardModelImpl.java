package edu.nju.model.impl;

import java.util.ArrayList;
import java.util.List;

import edu.nju.model.po.BlockPO;
import edu.nju.model.service.ChessBoardModelService;
import edu.nju.model.service.GameModelService;
import edu.nju.model.service.ParameterModelService;
import edu.nju.model.state.BlockState;
import edu.nju.model.state.GameResultState;
import edu.nju.model.state.GameState;
import edu.nju.model.vo.BlockVO;

public class ChessBoardModelImpl extends BaseModel implements ChessBoardModelService{
	
	private GameModelService gameModel;
	private ParameterModelService parameterModel;
	
	private BlockPO[][] blockMatrix;
	
	private int noMineBlock;
	private int count;
	private int remainMineNum;
	private int realRemainMineNum;
	List<BlockPO> blocks;
	
	public static boolean isFirstClick = true;
	public static boolean judge = true;
	public static boolean isOnNet = false;
	public static boolean isClient = false;
	
	public static int hostMarkNum = 0;
	public static int clientMarkNum = 0;
			
	public ChessBoardModelImpl(ParameterModelService parameterModel){
		this.parameterModel = parameterModel;
	}

	@Override
	public boolean initialize(int width, int height, int mineNum) {
		// TODO Auto-generated method stub
		/********************简单示例初始化方法，待完善********************/
		
		blockMatrix = new BlockPO[width][height];
		setBlock(mineNum);
		
		noMineBlock = width*height - mineNum;
		count = 0;
		remainMineNum = mineNum;
		realRemainMineNum = mineNum;
		judge = true;
		
		this.parameterModel.setMineNum(mineNum);
		/***********请在删除上述内容的情况下，完成自己的内容****************/
		
		this.printBlockMatrix();
		
		return false;
	}

	@Override
	public boolean excavate(int x, int y) {
		// TODO Auto-generated method stub

		if(blockMatrix == null)
			return false;
	
		blocks = new ArrayList<BlockPO>();
		
		GameState gameState = excavateRecursion(x,y);
		
		super.updateChange(new UpdateMessage("excute",this.getDisplayList(blocks, gameState)));	

		return true;
	}
	
	public GameState excavateRecursion(int x,int y){
		
		GameState gameState = GameState.RUN;
		
        BlockPO block = blockMatrix[x][y];
        int width = blockMatrix.length;
		int height = blockMatrix[0].length;
		
		if(isFirstClick&&block.isMine()){
    		
			blockMatrix[x][y].setMine(false);
    		for(int temI=x-1;temI<=x+1;temI++)
    			for(int temJ=y-1;temJ<=y+1;temJ++){
    				if((temI>-1&&temI<width)&&(temJ>-1&&temJ<height)){
    					blockMatrix[temI][temJ].minusMine();
    				}
    			}
    		
    		int i;
    		int j = 0;
    		loop: for(i=0;i<width;i++)
    			for(j=0;j<height;j++)
    				if(!blockMatrix[i][j].isMine()){
    					blockMatrix[i][j].setMine(true);
    					break loop;
    				}
    		
    		for(int temI=i-1;temI<=i+1;temI++)
    			for(int temJ=j-1;temJ<=j+1;temJ++){
    				if((temI>-1&&temI<width)&&(temJ>-1&&temJ<height)){
    					blockMatrix[temI][temJ].addMine();
    				}
    			}
    		
    		isFirstClick = false;
    	}
		
		if(judge){
		    if(blockMatrix[x][y].getState()==BlockState.UNCLICK&&!block.isMine()){
			    count++;
		        block.setState(BlockState.CLICK);
		        blockMatrix[x][y].setState(BlockState.CLICK);
		        blocks.add(block);
		        isFirstClick = false;
		    }
		}
		
		
		
		if(block.isMine()&&blockMatrix[x][y].getState()==BlockState.UNCLICK){
			gameState = GameState.OVER;
			blockMatrix[x][y].setState(BlockState.CLICK);
			if(isClient&&isOnNet)
				this.gameModel.gameOver(GameResultState.HOSTWIN);
			else
				if(!isClient&&isOnNet)
					this.gameModel.gameOver(GameResultState.CLIENTWIN);
				else
			    this.gameModel.gameOver(GameResultState.FAIL);
			for(int i=0;i<width;i++)
				for(int j=0;j<height;j++)
					//if(!(i==x&&j==y))
					blocks.add(blockMatrix[i][j]);
			judge = false;
		}
		
		
		if(noMineBlock==count&&!isOnNet){
			gameState = GameState.OVER;
			this.gameModel.gameOver(GameResultState.SUCCESS);
			for(int i=0;i<width;i++)
				for(int j=0;j<height;j++)
					if(blockMatrix[i][j].isMine())
						blocks.add(blockMatrix[i][j]);
			judge = false;
		}
		
		if(block.getMineNum()==0)
			for(int i=x-1;i<=x+1;i++)
				for(int j=y-1;j<=y+1;j++)
					if((i>-1&&i<width)&&(j>-1&&j<height)&&blockMatrix[i][j].getState()==BlockState.UNCLICK)
						excavateRecursion(i,j);
		
		return gameState;
	}
	
	@Override
	public boolean mark(int x, int y) {
		// TODO Auto-generated method stub

		if(blockMatrix == null)
			return false;
		
		GameState gameState = GameState.RUN;
		
		List<BlockPO> blocks = new ArrayList<BlockPO>();
		BlockPO block = blockMatrix[x][y];
		 
		BlockState state = block.getState();
		if(judge)
		if(state == BlockState.UNCLICK&&remainMineNum>0){
			if(isClient&&isOnNet){
			    block.setState(BlockState.CLIENTFLAG);
			    blockMatrix[x][y].setState(BlockState.CLIENTFLAG);
			    if(!blockMatrix[x][y].isMine()){
			    	this.gameModel.gameOver(GameResultState.HOSTWIN);
			    	gameState = GameState.OVER;
			    	judge = false;
			    }
			    else{
			    	this.clientMarkNum++;
			    	this.realRemainMineNum--;
			    	super.updateChange(new UpdateMessage("markcountchange", hostMarkNum+";"+clientMarkNum));
			    }
			}
			else
			    if(!isClient&&isOnNet){
				    block.setState(BlockState.FLAG);
				    blockMatrix[x][y].setState(BlockState.FLAG);
				    if(!blockMatrix[x][y].isMine()){
			    	    this.gameModel.gameOver(GameResultState.CLIENTWIN);
			    	    gameState = GameState.OVER;
			    	    judge = false;
				    }
				    else{
				    	this.hostMarkNum++;
				    	this.realRemainMineNum--;
				    	super.updateChange(new UpdateMessage("markcountchange", hostMarkNum+";"+clientMarkNum));
				    }
			    }
			    else{
			    	block.setState(BlockState.FLAG);
				    blockMatrix[x][y].setState(BlockState.FLAG);
				    if(blockMatrix[x][y].isMine())
				    	this.realRemainMineNum--;
			    }
			this.parameterModel.minusMineNum();
			remainMineNum--;
		}
		
		else if(state == BlockState.FLAG&&!isOnNet){
			block.setState(BlockState.UNCLICK);
			blockMatrix[x][y].setState(BlockState.UNCLICK);
			this.parameterModel.addMineNum();
			remainMineNum++;
			if(blockMatrix[x][y].isMine())
				this.realRemainMineNum++;
		}
		
		if(this.realRemainMineNum==0)
			this.gameModel.gameOver(GameResultState.SUCCESS);
		
		blocks.add(block);	
		super.updateChange(new UpdateMessage("excute",this.getDisplayList(blocks, gameState)));
		
		return true;
	}
	

	@Override
	public boolean quickExcavate(int x, int y) {
		// TODO Auto-generated method stub
		
		int markCount=0;
		int width = blockMatrix.length;
		int height = blockMatrix[0].length;
		
		if(blockMatrix[x][y].getState()==BlockState.CLICK)
		    for(int i=x-1;i<=x+1;i++)
			    for(int j=y-1;j<=y+1;j++)
			    	if((i>-1&&i<width)&&(j>-1&&j<height)&&(blockMatrix[i][j].getState()==BlockState.FLAG || blockMatrix[i][j].getState()==BlockState.CLIENTFLAG))
			    		markCount++;
		
		if(markCount==blockMatrix[x][y].getMineNum()){
			blocks = new ArrayList<BlockPO>();
			handleQuickExcavate(x,y);
			if(judge)
		        super.updateChange(new UpdateMessage("excute",this.getDisplayList(blocks, GameState.RUN)));
			else
				super.updateChange(new UpdateMessage("excute",this.getDisplayList(blocks, GameState.OVER)));
		}
		    	    

		return true;
	}
	
	public boolean handleQuickExcavate(int x,int y){
		
		int width = blockMatrix.length;
		int height = blockMatrix[0].length;
		
		for(int i=x-1;i<=x+1;i++)
	        for(int j=y-1;j<=y+1;j++)
	    	    if((i>-1&&i<width)&&(j>-1&&j<height)&&blockMatrix[i][j].getState()==BlockState.UNCLICK){
	    	    	excavateRecursion(i,j);
	    	    }
		
		return true;
	}

	/**
	 * 示例方法，可选择是否保留该形式
	 * 
	 * 初始化BlockMatrix中的Block，并随机设置mineNum颗雷
	 * 同时可以为每个Block设定附近的雷数
	 * @param mineNum
	 * @return
	 */
	private boolean setBlock(int mineNum){
		int width = blockMatrix.length;
		int height = blockMatrix[0].length;
		
		int temMineNum = mineNum;
		
		//初始化及布雷
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
				blockMatrix[i][j] = new BlockPO(i,j);
		
		while(temMineNum>0){
			
			int ranNum = (int) (Math.random()*width*height);
			if(blockMatrix[ranNum/height][ranNum%height].isMine()==false){
				blockMatrix[ranNum/height][ranNum%height].setMine(true);
				addMineNum(ranNum/height,ranNum%height);
				temMineNum--;
			}
			
		}
		
		return false;
	}
	
	
	/**
	 * 示例方法，可选择是否保留该形式
	 * 
	 * 将(i,j)位置附近的Block雷数加1
	 * @param i
	 * @param j
	 */
	private void addMineNum(int i, int j){
		int width = blockMatrix.length;
		int height = blockMatrix[0].length;
		
		
		for(int temI=i-1;temI<=i+1;temI++)
			for(int temJ=j-1;temJ<=j+1;temJ++){
				if((temI>-1&&temI<width)&&(temJ>-1&&temJ<height)){
					blockMatrix[temI][temJ].addMine();
				}
			}
		
	}
	
	/**
	 * 将逻辑对象转化为显示对象
	 * @param blocks
	 * @param gameState
	 * @return
	 */
	private List<BlockVO> getDisplayList(List<BlockPO> blocks, GameState gameState){
		List<BlockVO> result = new ArrayList<BlockVO>();
		for(BlockPO block : blocks){
			if(block != null){
				BlockVO displayBlock = block.getDisplayBlock(gameState);
				if(displayBlock.getState() != null)
				result.add(displayBlock);
			}
		}
		return result;
	}

	@Override
	public void setGameModel(GameModelService gameModel) {
		// TODO Auto-generated method stub
		this.gameModel = gameModel;
	}
	
	private void printBlockMatrix(){
		for(BlockPO[] blocks : this.blockMatrix){
			for(BlockPO b :blocks){
				String p = b.getMineNum()+"";
				if(b.isMine())
					p="*";
				System.out.print(p);
			}
			System.out.println();
		}
	}
	
	public ParameterModelService getParameterModel(){
		return this.parameterModel;
	}
	
}
