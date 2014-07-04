package br.com.cseabra.liquigui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.cseabra.liquigui.liquibase.LiquibaseCommand;

public class PnlParameter extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTextField textField = new JTextField();
	private final JComboBox<String> comboBox = new JComboBox<String>();

	/**
	 * Create the panel.
	 */
	public PnlParameter() {
		buildGUI(LiquibaseCommand.DEFAULT, null, null);
	}
	public PnlParameter(final LiquibaseCommand command, final Container parentContainer, ActionListener removePnlParemeterListener) {
		buildGUI(command, parentContainer, removePnlParemeterListener);
	}

	private void buildGUI(final LiquibaseCommand command, final Container parentContainer, final ActionListener removePnlParemeterListener) {
		JLabel lblParametro = new JLabel("Parâmetro: ");
		JLabel lblValor = new JLabel("Valor: ");

		add(lblParametro);
		add(comboBox);
		add(lblValor);
		add(textField);
		comboBox.setModel(new DefaultComboBoxModel<String>(command.getParams()));
		textField.setColumns(10);
		
		JButton btnDelete = new JButton("X");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				parentContainer.remove(PnlParameter.this);
				if(removePnlParemeterListener != null) {
					removePnlParemeterListener.actionPerformed(arg0);
				}
				parentContainer.validate();
				parentContainer.repaint();
			}
		});
		add(btnDelete);
	}

	public String getSelectedParemeter(){
		return comboBox.getSelectedItem().toString();
	}
	
	public String getParameterValue(){
		return textField.getText(); 
	}
}
