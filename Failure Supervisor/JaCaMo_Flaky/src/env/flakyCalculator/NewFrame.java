package flakyCalculator;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NewFrame extends JFrame {

	private JPanel contentPane;
	public JTextField textField;
	public JTextField resultTextField;
	public JButton calculateButton;
	public JButton clearTextButton;
	public JButton stopWorkingButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewFrame frame = new NewFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NewFrame() {
		
		
		setTitle("Flaky Expression Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		//setBounds(100, 100, 450, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel panel1 = new JPanel();
		panel1.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		contentPane.add(panel1);
		panel1.setLayout(new GridLayout(3, 1, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Expression Calculator: Allowed Operator");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel1.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Possible Keyword: multiply(n1,n2), divide(n1,n2), add(n1,n2), const(n1)");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel1.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Example: add(Const(3), Const(2))");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel1.add(lblNewLabel_2);
		JPanel panel2 = new JPanel();
		panel2.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		contentPane.add(panel2);
		panel2.setLayout(new GridLayout(3, 1, 0, 0));
		
		JPanel panel3 = new JPanel();
		panel2.add(panel3);
		
		JLabel lblInput = new JLabel("Expression Input");
		panel3.add(lblInput);
		lblInput.setHorizontalAlignment(SwingConstants.CENTER);
		
		textField = new JTextField();
		panel3.add(textField);
		textField.setColumns(20);
		
		JPanel panel4 = new JPanel();
		panel2.add(panel4);
		
		calculateButton = new JButton("Calculate");
		calculateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			resultTextField.setText("waiting for result");
			}
		});
		panel4.add(calculateButton);
		
		clearTextButton = new JButton(" Clear Input Field");
		clearTextButton.setToolTipText("Clear Input Text Field");
		clearTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
			}
		});
		panel4.add(clearTextButton);
		
		stopWorkingButton = new JButton(" Reset ");
		clearTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
			}
		});
		panel4.add(stopWorkingButton);
		
		JPanel panel5 = new JPanel();
		panel2.add(panel5);
		
		JLabel lblNewLabel_3 = new JLabel("Result");
		panel5.add(lblNewLabel_3);
		
		resultTextField = new JTextField();
		panel5.add(resultTextField);
		resultTextField.setColumns(10);
	}

}
