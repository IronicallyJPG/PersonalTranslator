
package com.ironically.googletranslate.main;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import Main.*;

public class Window extends JFrame {
	
	public static Window GUI;
	
	/**
	 * References for the project.
	 * 
	 */
	private static final long	serialVersionUID				= -1611063499735712151L;
	private JPanel				contentPane;
	private static final File	GOOGLE_APPLICATION_CREDENTIALS	= new File("keys.json");
	private JTextField			txtInput;
	private JTextField			txtOutput;
	public JProgressBar			progressBar;
	private boolean				runProgressUpdater				= false;
	private JTextArea			langchain;
	private Choice				langchoice;
	private JButton				btnTranslate;
	
	/*
	 * This Runnable is the only way I can seem to update the progress bar AND
	 * Continue the translating process.
	 */
	Runnable Progrunnable;
	
	/**
	 * Launch the application.
	 */
	public static void LaunchGUI() {
		EventQueue.invokeLater(() -> {
			try {
				if (Data.isGui == false) throw new Exception("GUI IS NOT ENABLED!");
				GUI = new Window();
				Tools.GoogleAuth(GOOGLE_APPLICATION_CREDENTIALS.getAbsolutePath());
				GUI.setVisible(true);
				Data.LanguageSetup();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public Window() {
		// ===========================================================================
		/*
		 * Defines the Runnable above
		 */
		Progrunnable = () -> {
			// LL.console("PROGRESS THREAD STARTED");
			while (runProgressUpdater == true) {
				progressBar.setValue(Data.progress);
				try {
					Thread.sleep(200);
				}
				catch (InterruptedException e) {
					return;
				}
			}
			// LL.console("PROGRESS THREAD STOPPED");
		};
		// ===========================================================================
		setResizable(false);
		setTitle("Translator Window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 529);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.desktop);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnTranslate = new JButton("SUBMIT");
		btnTranslate.setBounds(300, 53, 126, 42);
		btnTranslate.setFont(new Font("Consolas", Font.BOLD, 12));
		btnTranslate.setForeground(new Color(0, 0, 205));
		btnTranslate.setBackground(new Color(0, 255, 0));
		contentPane.add(btnTranslate);
		
		txtInput = new JTextField();
		txtInput.setBounds(95, 21, 86, 20);
		txtInput.setBackground(new Color(0, 153, 51));
		txtInput.setEditable(false);
		txtInput.setText("INPUT");
		txtInput.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(txtInput);
		txtInput.setColumns(10);
		
		txtOutput = new JTextField();
		txtOutput.setBounds(548, 21, 86, 20);
		txtOutput.setBackground(new Color(204, 102, 51));
		txtOutput.setEditable(false);
		txtOutput.setHorizontalAlignment(SwingConstants.CENTER);
		txtOutput.setText("OUTPUT");
		contentPane.add(txtOutput);
		txtOutput.setColumns(10);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(191, 22, 347, 20);
		progressBar.setStringPainted(true);
		progressBar.setToolTipText("Progress");
		progressBar.setMaximum(10);
		contentPane.add(progressBar);
		
		langchoice = new Choice();
		langchoice.setBounds(300, 101, 126, 20);
		langchoice.addItemListener(evt -> {
			LL.console("LANGUAGE LAYERS: " + langchoice.getSelectedItem());
			Data.updateProgressBarData_GUI(progressBar, Integer.parseInt(langchoice.getSelectedItem().substring(0, 2).trim()));
			progressBar.setValue(0);
		});
		for (int i = 2; i < 51; i++) {
			if (i % 2 == 0) {
				langchoice.add(i + " Languages");
			}
		}
		langchoice.select(3);
		contentPane.add(langchoice);
		
		JScrollPane inputPane = new JScrollPane();
		inputPane.setBounds(10, 53, 280, 386);
		contentPane.add(inputPane);
		
		JTextArea INPUT_AREA = new JTextArea();
		INPUT_AREA.setBackground(new Color(0, 128, 128));
		INPUT_AREA.setToolTipText("INPUT TEXT TO BE TRANSLATED");
		INPUT_AREA.setFont(new Font("Consolas", Font.PLAIN, 15));
		inputPane.setViewportView(INPUT_AREA);
		
		JScrollPane outputPane = new JScrollPane();
		outputPane.setBounds(436, 53, 248, 386);
		contentPane.add(outputPane);
		
		JTextArea OUTPUT_AREA = new JTextArea();
		OUTPUT_AREA.setBackground(new Color(46, 139, 87));
		OUTPUT_AREA.setToolTipText("OUTPUT OF TRANSLATED TEXT");
		OUTPUT_AREA.setFont(new Font("Consolas", Font.PLAIN, 15));
		OUTPUT_AREA.setEditable(false);
		outputPane.setViewportView(OUTPUT_AREA);
		
		JFormattedTextField InfoBox = new JFormattedTextField();
		InfoBox.setToolTipText("Credits/Info");
		InfoBox.setEditable(false);
		InfoBox.setFont(new Font("Consolas", Font.PLAIN, 18));
		InfoBox.setText("VERSION: 1.0.1 || AUTHOR:  IronicallyJPG || API: GOOGLE TRANSLATE ");
		InfoBox.setBounds(10, 450, 674, 42);
		contentPane.add(InfoBox);
		
		JCheckBox useAll = new JCheckBox("ALL LANGUAGES");
		useAll.addItemListener(e -> {
			if (useAll.isSelected() == true) {
				langchoice.setEnabled(false);
				Data.useAll = true;
				Data.updateProgressBarData_GUI(progressBar, Data.ALL_LANGS.length);
				
			}
			if (useAll.isSelected() == false) {
				Data.useAll = false;
				langchoice.setEnabled(true);
				langchoice.select(1);
				Data.updateProgressBarData_GUI(progressBar, Integer.parseInt(langchoice.getSelectedItem().substring(0, 2).trim()));
				Data.LanguageSetup();
			}
		});
		useAll.setBounds(300, 127, 126, 23);
		contentPane.add(useAll);
		
		JScrollPane langpane = new JScrollPane();
		langpane.setBounds(300, 157, 126, 282);
		contentPane.add(langpane);
		
		langchain = new JTextArea();
		langchain.setWrapStyleWord(true);
		langchain.setLineWrap(true);
		langpane.setViewportView(langchain);
		langchain.setBackground(Color.LIGHT_GRAY);
		langchain.setFont(new Font("Consolas", Font.BOLD, 12));
		langchain.setEditable(false);
		
		btnTranslate.addActionListener(e -> {
			langchoice.setEnabled(false);
			this.setEnabled(false);
			if (INPUT_AREA.getText().trim().length() < 2) {
				LL.console("INCORRECT INPUT!!!");
			}
			else {
				try {
					runProgressUpdater = true;
					new Thread(Progrunnable).start();
					Data.LanguageSetup();
					setLangChain();
					OUTPUT_AREA.setText(Tools.TranslateGivenText(INPUT_AREA.getText().trim()));
				}
				catch (IOException e1) {
					setLangChain();
					OUTPUT_AREA.setText("UNABLE TO TRANSLATE!!! CHECK LOG!");
					e1.printStackTrace();
				}
			}
			this.setEnabled(true);
			runProgressUpdater = false;
			langchoice.setEnabled(true);
		});
		
	}
	
	/*
	 * Increments the progress by 1
	 */
	public void ProgressUpdate() {
		Data.progress++;
	}
	
	/*
	 * A Resetter for the progress bar
	 */
	public void ProgressRESET() {
		Data.progress = 0;
	}
	
	/*
	 * Sets the Language Chain GUI Text
	 */
	public void setLangChain() {
		langchain
				.setText("LANGUAGE CHAIN:\n" + Arrays.toString(Data.CODES.toArray()).replace(", ", " > ").replace("[", " ").replace("]", " ").trim());
	}
}
