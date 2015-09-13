/*
 *
 * TODO To manage menu action
 */
package edu.nju.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.sun.xml.internal.ws.transport.http.server.ServerAdapter;

import edu.nju.controller.impl.ClientControllerImpl;
import edu.nju.controller.impl.HostControllerImpl;
import edu.nju.controller.impl.MenuControllerImpl;
import edu.nju.controller.impl.SettingControllerImpl;
import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.controller.service.ClientControllerService;
import edu.nju.controller.service.HostControllerService;
import edu.nju.controller.service.MenuControllerService;
import edu.nju.controller.service.SettingControllerService;
import edu.nju.model.impl.ChessBoardModelImpl;
import edu.nju.model.impl.GameModelImpl;
import edu.nju.model.impl.ParameterModelImpl;
import edu.nju.model.impl.StatisticModelImpl;
import edu.nju.network.Configure;
import edu.nju.network.client.ClientAdapter;
import edu.nju.network.client.ClientInHandlerImpl;
import edu.nju.network.client.ClientServiceImpl;
import edu.nju.network.host.HostInHandlerImpl;
import edu.nju.network.host.HostServiceImpl;
import edu.nju.network.modelProxy.GameModelProxy;
import edu.nju.view.CustomDialog;
import edu.nju.view.MainFrame;
import edu.nju.view.RecordDialog;
 

public class MenuListener implements ActionListener{

	private MainFrame ui;
	MenuControllerService menuController = new MenuControllerImpl();
	SettingControllerService settingController = new SettingControllerImpl();
	HostControllerService hostController = new HostControllerImpl();
	ClientControllerService clientController = new ClientControllerImpl();
	
	public MenuListener(MainFrame ui){
		this.ui = ui;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == ui.getMenuItem("start")) {//生成游戏，默认生成简单游戏
			
			menuController.startGame();
			
		} else if (e.getSource() == ui.getMenuItem("easy")) {//生成简单游戏
			
			settingController.setEasyGameLevel();
			
		} else if (e.getSource() == ui.getMenuItem("hard")) {//生成中等游戏
			
			settingController.setHardGameLevel();
			
		} else if (e.getSource() == ui.getMenuItem("hell")) {//生成大型游戏
			
			settingController.setHellGameLevel();
			
		} else if (e.getSource() == ui.getMenuItem("custom")) {//生成定制游戏，需要向controller传递棋盘的高、宽和雷数
			
			CustomDialog customDialog = new CustomDialog(ui.getMainFrame());
			if(customDialog.show()){
				settingController.setCustomizedGameLevel(customDialog.getHeight(), customDialog.getWidth(), customDialog.getMineNumber());
			}

		} else if (e.getSource() == ui.getMenuItem("exit")) {
			System.exit(0);
		} else if (e.getSource() == ui.getMenuItem("record")) {//统计胜率信息
			
			RecordDialog recordDialog = new RecordDialog(ui.getMainFrame());
			recordDialog.show();
			
		} else if(e.getSource() == ui.getMenuItem("host")){//注册成为主机
			
			hostController.serviceetupHost();
			
		} else if(e.getSource() == ui.getMenuItem("client")){//注册成为客户端
			
			clientController.setupClient(Configure.SERVER_ADDRESS);
			
		}else if(e.getSource() == ui.getMenuItem("close")){//结束联网
			
			if(ChessBoardModelImpl.isOnNet&&OperationQueue.isClient)
				ClientAdapter.close();
			if(ChessBoardModelImpl.isOnNet&&!OperationQueue.isClient)
				edu.nju.network.host.ServerAdapter.close();
			
		}
	}


}