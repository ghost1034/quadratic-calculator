package com.quadratic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Frame extends JFrame {

	private JPanel panelTop = new JPanel();
	private JPanel panelCenter = new JPanel();
	private JPanel panelBottom = new JPanel();
	private JPanel panelNumpad = new JPanel();
	private JPanel panelSympad = new JPanel();
	private JTextArea resultBox = new JTextArea();
	private Graph graph = new Graph(HEIGHT / 3, HEIGHT / 3, 15);
	private JTextField inputBox = new JTextField();
	private String[] nums = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "C"};
	private String[] syms = new String[] {"⌫", "+", "-", "x^2", "x", "="};
	private JButton[] buttons = new JButton[nums.length + syms.length];
	private Font font = new Font(Font.MONOSPACED, Font.PLAIN, 18);
	private Quadratic quadratic = new Quadratic();
	private static final int HEIGHT = 700;
	private static final int WIDTH = 600;
	private static final long serialVersionUID = 868271417414176101L;
	
	public static void main(String[] args) {
		new Frame();
	}
	
	public Frame() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setupComponents();
		setupListeners();
	}
	
	private void setupComponents() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		panelTop.setLayout(new GridLayout(1, 2));
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
		panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.X_AXIS));
		panelNumpad.setLayout(new GridLayout(4, 3));
		panelSympad.setLayout(new GridLayout(syms.length, 1));
		
		add(panelTop);
		add(panelCenter);
		add(panelBottom);
		panelTop.add(resultBox);
		panelTop.add(graph);
		panelCenter.add(inputBox);
		panelBottom.add(panelNumpad);
		panelBottom.add(panelSympad);
		
		setupButtons();
		
		panelBottom.setPreferredSize(new Dimension(WIDTH, (int) (HEIGHT / 1.75)));
		panelNumpad.setPreferredSize(new Dimension((int) (WIDTH / 1.5), panelBottom.getPreferredSize().height));
		resultBox.setPreferredSize(new Dimension(WIDTH / 2, HEIGHT - panelBottom.getPreferredSize().height));
		resultBox.setMinimumSize(resultBox.getPreferredSize());
		inputBox.setPreferredSize(new Dimension(WIDTH, HEIGHT / 9));
		inputBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, inputBox.getPreferredSize().height));
		
		resultBox.setFont(font);
		inputBox.setFont(font);
		
		resultBox.setEditable(false);
		
		setTitle("Quadratic Calculator");
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH + 510, HEIGHT + 290));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void setupButtons() {
		String[] nums = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "C"};
		String[] syms = new String[] {"⌫", "+", "-", "x^2", "x", "="};
		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton();
			String text;
			
			if (i < nums.length) {
				text = nums[i];
				panelNumpad.add(buttons[i]);
			} else {
				text = syms[i - nums.length];
				panelSympad.add(buttons[i]);
			}
			
			buttons[i].setText(text);
			buttons[i].setFont(font);
			buttons[i].setBackground(Color.WHITE);
			buttons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (text.equals("C")) {
						inputBox.setText("");
					} else if (text.equals("⌫")) {
						inputBox.select(inputBox.getCaretPosition() - 1, inputBox.getCaretPosition());
						inputBox.replaceSelection("");
					} else {
						inputBox.replaceSelection(text);
					}
				}
			});
		}
	}
	
	private void setupListeners() {
		inputBox.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateResult();
			}
			
			public void insertUpdate(DocumentEvent e) {
				updateResult();
			}
			
			public void removeUpdate(DocumentEvent e) {
				updateResult();
			}
			
			public void updateResult() {
				if (inputBox.getText().isBlank()) {
					quadratic = new Quadratic();
					resultBox.setText("");
					graph.repaint();
				} else {
					String equation = inputBox.getText().trim() + (!inputBox.getText().contains("=") ? ("=0") : "");
					quadratic = new Quadratic(equation);
					
					if (quadratic.isValid()) {
						String numerator = quadratic.getNegativeB() + " ± " + quadratic.getDiscriminant().getSquareRoot();
						String denominator = quadratic.getTwoA();
						String x1 = quadratic.getFirstX();
						String x2 = quadratic.getSecondX();
						String result = equation + "\n" + "\n"
								+ Utilities.repeatString(" ", denominator.length() / 2 - numerator.length() / 2) + numerator + "\n" 
								+ Utilities.repeatString("—", (numerator.length() > denominator.length()) ? numerator.length() : denominator.length()) + "\n" 
								+ Utilities.repeatString(" ", numerator.length() / 2 - denominator.length() / 2) + denominator + "\n" + "\n" 
								+ "x = " + x1 + "\n" 
								+ "x = " + x2;
						resultBox.setText(result);
						graph.repaint();
					}
				}
			}
		});
		
		addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				updateGraphSize();
			}
			
			public void componentHidden(ComponentEvent e) {
				updateGraphSize();
			}
			
			public void componentMoved(ComponentEvent e) {
				updateGraphSize();
			}
			
			public void componentShown(ComponentEvent e) {
				updateGraphSize();
			}
			
			public void updateGraphSize() {
				graph.updateSize(getWidth() - resultBox.getWidth(), resultBox.getHeight());
			}
		});
	}
	
	private class Graph extends JPanel {

		private Graphics2D g2d;
		private int width;
		private int height;
		private int scale;
		private int widthCenter;
		private int heightCenter;
		private List<Integer> xCoords = new ArrayList<>();
		private List<Integer> yCoords = new ArrayList<>();
		private static final long serialVersionUID = 5593974461737118408L;

		public Graph(int width, int height, int scale) {
			setPreferredSize(new Dimension(width, height));
			this.width = width;
			this.height = height;
			this.scale = scale;
		}
		
		public void updateSize(int width, int height) {
			this.width = width;
			this.height = height;
			repaint();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			drawGraph();
			drawParabola();
		}
		
		private int[] listToArray(List<Integer> list) {
			int[] result = new int[list.size()];
			for (int i = 0; i < result.length; i++) {
			    result[i] = list.get(i);
			}
			return result;
		}
		
		private void drawGraph() {
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.setStroke(new BasicStroke(1));
			
			for (int i = 0; i < height; i += scale) {
				if (i / scale == height / scale / 2) {
					heightCenter = i;
				}
				g2d.drawLine(0, i, width, i);
			}
			
			for (int i = 0; i < width; i += scale) {
				if (i / scale == width / scale / 2) {
					widthCenter = i;
				}
				g2d.drawLine(i, 0, i, height);
			}
			
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawLine(0, heightCenter, width, heightCenter);
			g2d.drawLine(widthCenter, 0, widthCenter, height);
		}
		
		private void drawParabola() {
			g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
			
			if (quadratic.isValid()) {
				g2d.setColor(Color.RED);
				double x = -1 * width / scale;
				double y;
				
				while (x < width / scale) {
					y = quadratic.getY(x);
					xCoords.add((int) (widthCenter + x * scale) + 1);
					yCoords.add((int) (heightCenter - y * scale) + 1);
					x += 0.2;
				}
				
				g2d.drawPolyline(listToArray(xCoords), listToArray(yCoords), xCoords.size());
			}
			
			xCoords.clear();
			yCoords.clear();
		}
		
	}
	
}