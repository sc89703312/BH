package edu.nju.model.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import edu.nju.model.service.StatisticModelService;
import edu.nju.model.state.GameResultState;

public class StatisticModelImpl extends BaseModel implements StatisticModelService{

	private int sum = 0;
	private int wins = 0;
	private int minTime = 999;
	private File saveFile = new File("save.txt");
	private String[] levels = {"小","中","大","自定义"};
	private String[] records = new String[4];
	private String[] temString = new String[4];
	
	public StatisticModelImpl() throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(saveFile));
		if(!br.readLine().split(";")[0].equals("小")){
			br.close();
		    BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
		    for(int i=0;i<=3;i++){
			    bw.write(levels[i]+";"+minTime+";"+wins+";"+sum+"\n");
		    }
		    bw.close();
		}
		else
			br.close();
		
	}
	
	@Override
	public void recordStatistic(GameResultState result, int time) {
		// TODO Auto-generated method stub	
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(saveFile));
			for(int i=0;i<=3;i++){
				records[i] = br.readLine();
				temString = records[i].trim().split(";");
				if(temString[0].equals(GameModelImpl.currentLevel)){
					if(result==GameResultState.FAIL)
						temString[3] = Integer.toString(Integer.parseInt(temString[3])+1);
					if(result==GameResultState.SUCCESS){
						temString[3] = Integer.toString(Integer.parseInt(temString[3])+1);
						temString[2] = Integer.toString(Integer.parseInt(temString[2])+1);
						if(time<Integer.parseInt(temString[1]))
							temString[1] = Integer.toString(time);
					}
					records[i] = temString[0]+";"+temString[1]+";"+temString[2]+";"+temString[3];
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			BufferedWriter bw  = new BufferedWriter(new FileWriter(saveFile));
			for(int i=0;i<4;i++)
				bw.write(records[i]+"\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void recordStatistic(GameResultState result, String level, int time) {
		// TODO Auto-generated method stub
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(saveFile));
			for(int i=0;i<=3;i++){
				records[i] = br.readLine();
				temString = records[i].trim().split(";");
				if(temString[0].equals(level)){
					if(result==GameResultState.FAIL)
						temString[3] = Integer.toString(Integer.parseInt(temString[3])+1);
					if(result==GameResultState.SUCCESS){
						temString[3] = Integer.toString(Integer.parseInt(temString[3])+1);
						temString[2] = Integer.toString(Integer.parseInt(temString[2])+1);
						if(time<Integer.parseInt(temString[1]))
							temString[1] = Integer.toString(time);
					}
					records[i] = temString[0]+";"+temString[1]+";"+temString[2]+";"+temString[3];
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			BufferedWriter bw  = new BufferedWriter(new FileWriter(saveFile));
			for(int i=0;i<4;i++)
				bw.write(records[i]+"\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void showStatistics() {
		// TODO Auto-generated method stub
		
	}

}
