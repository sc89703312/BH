package edu.nju.model.impl;

import java.util.Observable;
import java.util.Observer;

import edu.nju.network.TransformObject;

/**
 * 基础Model类，继承Obsevvale方法，基础Observable接口类可以向其注册，监听数据变化
 * @author Wangy
 *
 */
public class BaseModel extends Observable implements Observer{

	/**
	 * 通知更新方法，请在子类中需要通知观察者的地方调用此方法
	 * @param data
	 */
	protected void updateChange(UpdateMessage message){
		
		super.setChanged();
		super.notifyObservers(message);
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
		TransformObject transformObject = (TransformObject)arg1;
		UpdateMessage msge = transformObject.getMsg();
		this.updateChange(msge);
		
	}
	
}