package br.com.cseabra.liquigui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.cseabra.liquigui.liquibase.Commands;

public class PnlParameter extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTextField textField = new JTextField();
	private final JComboBox<String> comboBox = new JComboBox<String>();

	/**
	 * Create the panel.
	 */
	public PnlParameter() {
		buildGUI(Commands.DEFAULT);
	}
	public PnlParameter(Commands command) {
		buildGUI(command);
	}

	private void buildGUI(Commands command) {
		JLabel lblParametro = new JLabel("Parâmetro: ");
		JLabel lblValor = new JLabel("Valor: ");

		add(lblParametro);
		add(comboBox);
		add(lblValor);
		add(textField);
		comboBox.setModel(new DefaultComboBoxModel<String>(command.getParams()));
		textField.setColumns(10);
	}

	public String getSelectedParemeter(){
		return comboBox.getSelectedItem().toString();
	}
	
	public String getParameterValue(){
		return textField.getText(); 
	}
}
