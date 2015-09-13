/*
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.nju.view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import edu.nju.model.service.StatisticModelService;

public class RecordDialog {
	
	File saveFile = new File("save.txt");
	String names[] = {"小","中","大","自定义"};

	/**
	 *  
	 */
	public RecordDialog(JFrame parent) {
		super();
		initialization(parent);
	}

	public boolean show() {
		clear = false;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(saveFile));
			for(int i=0;i<4;i++){
				String[] temString = br.readLine().trim().split(";");
				int sum = Integer.parseInt(temString[3]);
				int wins = Integer.parseInt(temString[2]);
				if(sum==0)
					winRates[i] = 0;
				else
				    winRates[i] = (double)wins/(double)sum;
				score[i] = Integer.parseInt(temString[1]);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dialog.setVisible(true);
		return clear;
	}

	private void initialization(JFrame parent) {

		dialog = new JDialog(parent, "record", true);

		okBtn = new JButton("ok");
		okBtn.setFont(new Font("Monospaced", Font.PLAIN, 12));
		okBtn.setBounds(100, 145, 70, 23);
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});

		clearBtn = new JButton("clear");
		clearBtn.setFont(new Font("Monospaced", Font.PLAIN, 12));
		clearBtn.setBounds(192, 145, 70, 23);
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear = true;
				int length = winRates.length;
				for (int i = 0; i != length; ++i) {
					winRates[i] = 0;
					score[i] = 999;
				}
				
				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
					for(int i=0;i<4;i++)
						bw.write(names[i]+";"+score[i]+";"+"0"+";"+"0"+"\n");
					bw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				textPanel.repaint();
			}
		});

		line = new JSeparator();
		line.setBounds(20, 105, 240, 4);

		panel = new JPanel();
		panel.setLayout(null);

		textPanel = new DescribeTextPanel();
		panel.add(textPanel);

		panel.add(okBtn);
		panel.add(clearBtn);
		panel.add(line);

		dialog.setContentPane(panel);
		dialog.setBounds(parent.getLocation().x + 50,
				parent.getLocation().y + 50, 320, 220);

		clear = false;

	}

	private class DescribeTextPanel extends JPanel {

		DescribeTextPanel() {
			super();
			setBounds(0, 0, 330, 140);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setFont(new Font("Monospaced", Font.PLAIN, 12));
			int length = names.length;
			for (int i = 0; i != length; i++) {
				double winRatePercent = ((int)(winRates[i]*10000))/100;
				g.drawString(Double.toString(winRatePercent)+"%", 20, 30 * (i + 1));
				g.drawString(String.valueOf(score[i]),150, 30 * (i + 1));
				g.drawString(rank[i], 230, 30 * (i + 1));
			}
		}
	}

	private final String[] rank = { "Easy", "Hard", "Hell", "Customized"};
  	private JDialog dialog;

	private JPanel panel;

	private JButton okBtn;

	private JButton clearBtn;

	private JSeparator line;
	
	private double winRates[] = new double[4];

	private int score[] = new int[4];

	private JPanel textPanel;

	boolean clear;
}