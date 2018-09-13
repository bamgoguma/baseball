package com.inc.baseball;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.InputMismatchException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BaseBall extends JFrame {
	
	private JPanel predictPanel;
	
	private JTextField firstField, secondField, thirdField;
	private JButton predictBtn, restartBtn;
	private JTextArea printArea;
	private JLabel scoreLabel;
	
	private Set<Integer> predicts;
	private Set<Integer> answers;
	
	private int strike, ball, score;
	
	
	private BaseBall() {
		setTitle("BaseBall - www.increpas.com");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 300, 500, 500);
		initComponent();
		initEvent();
		setAnswers();
		setVisible(true);
	}
	

	private void setAnswers() {
		predicts = new LinkedHashSet<>();
		answers = new LinkedHashSet<>();
		while(answers.size() < 3) {
			int r = (int)(Math.random() * 10);
			answers.add(r);
		}
		System.out.println(answers);
	}
	
	private void setPredicts() throws Exception {
		int first = Integer.parseInt(firstField.getText());
		int second = Integer.parseInt(secondField.getText());
		int third = Integer.parseInt(thirdField.getText());
		
		numberCheck(first);
		numberCheck(second);
		numberCheck(third);
		
		predicts.removeAll(predicts);
		
		predicts.add(first);
		predicts.add(second);
		predicts.add(third);
		
		if(predicts.size() < 3) {
			throw new Exception();
		}
	}
	
	private void numberCheck(int number) {
		if(number < 0 || number > 9) {
			throw new InputMismatchException("범위를 벗어난 숫자입니다.(범위:0~9)");
		}
	}
	
	private void compare() {
		strike = 0;
		ball = 0;
		int i = 0;
		for(int answer : answers) {
			int j = 0;
			for(int predict : predicts) {
				if(answer == predict) {
					if(i == j) {
						strike++;
					}else {
						ball++;
					}
				}
				j++;
			}
			i++;
		}
	}
	
	private void printResult() {
		StringBuffer sb = new StringBuffer();
		for(int predict : predicts) {
			sb.append(predict + "\t");
		}
		sb.append(" | "+strike+"s ");
		sb.append(ball+"b\n");
		printArea.append(sb.toString());
		firstField.setText("");
		secondField.setText("");
		thirdField.setText("");
		scoreLabel.setText("score : " + ++score);
	}
	
	private void ending() {
		String[] choices = {"다시시작", "종료"};
		int choice = JOptionPane.showOptionDialog(
				getContentPane(), "축하합니다", null, 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
		if(choice == 0) {
			restart();
		}else {
			System.exit(0);
		}
	}
	private void restart() {
		printArea.setText("");
		answers.removeAll(answers);
		setAnswers();
		score = 0;
		scoreLabel.setText("socre : "+score);
	}

	private void initEvent() {
		predictBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					setPredicts();
					compare();
					printResult();
					if(strike == 3) {
						ending();
					}
				}catch(InputMismatchException ie) {
					JOptionPane.showMessageDialog(
							getContentPane(), ie.getMessage());
				}catch(NumberFormatException ne) {
					JOptionPane.showMessageDialog(
							getContentPane(), "숫자만 입력해주세요");
				}catch(Exception ee) {
					JOptionPane.showMessageDialog(
							getContentPane(), "중복되지 않은 숫자들을 입력해 주세요");
				}
			}
		});
		
		restartBtn.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				restart();
			}
			
		});
	}


	private void initComponent() {
		predictPanel = new JPanel();
		
		firstField = new JTextField(3);
		secondField = new JTextField(3);
		thirdField = new JTextField(3);
		
		predictBtn = new JButton("Predict!");
		restartBtn = new JButton("Restart!");
		
		scoreLabel = new JLabel("Score : " + score);
		
		predictPanel.add(firstField);
		predictPanel.add(secondField);
		predictPanel.add(thirdField);
		predictPanel.add(predictBtn);
		predictPanel.add(restartBtn);
		predictPanel.add(scoreLabel);
		
		add(predictPanel, BorderLayout.NORTH);
		
		printArea = new JTextArea();
		//바탕-검정, 글씨-흰색
		printArea.setBackground(Color.BLACK);
		printArea.setForeground(Color.WHITE);
		printArea.setEditable(false);
		add(printArea);
	}


	public static void main(String[] args) {
		new BaseBall();
	}

}
