package flakyCalculator;
// CArtAgO artifact code for project flakyExpressionCalculator

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import cartago.*;
import cartago.tools.GUIArtifact;

public class GuiArtifact extends GUIArtifact {
	private NewFrame frame;
	public void setup() {
		frame = new NewFrame(); 
		linkActionEventToOp(frame.calculateButton,"calculate");
		linkActionEventToOp(frame.clearTextButton,"clearText");
		linkActionEventToOp(frame.stopWorkingButton, "reset");
		linkWindowClosingEventToOp(frame, "closed");
		defineObsProperty("expression", " ");
		frame.setVisible(true);
	}


	@INTERNAL_OPERATION void calculate(ActionEvent ev){
		String  expr = frame.textField.getText();
		getObsProperty("expression").updateValue(expr);
		signal("calculate");
	}
	
	@INTERNAL_OPERATION void reset(ActionEvent ev){
		signal("reset");
	}

	@INTERNAL_OPERATION void closed(WindowEvent ev){
		signal("closed");
		
	}
	
	@INTERNAL_OPERATION void clearText(ActionEvent ev){
		getObsProperty("expression").updateValue("");
	}
	
	@OPERATION void setResult(String s){
		frame.resultTextField.setText(s);
	}
	

}


