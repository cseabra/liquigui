package br.com.cseabra.liquigui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import br.com.cseabra.liquigui.liquibase.LiquibaseCommand;

public class Liquigui extends JFrame {
	private static final String LIQUIBASE_EXECUTABLE = "liquibase.bat ";
	private static final long serialVersionUID = 1L;
	private final JPanel contentPane = new JPanel();
	private final JLabel lblLiquigui = new JLabel("Liquigui");
	private final JPanel pnlCenter = new JPanel();
	private final JPanel pnlCommand = new JPanel();
	private final JLabel lblEscolhaOComando = new JLabel("Escolha o comando");
	private final JPanel pnlParameters = new JPanel();
	private final JComboBox<LiquibaseCommand> cbxCommands = new JComboBox<LiquibaseCommand>();
	private final JButton btnAddParemeter = new JButton("Adicionar parâmetro");
	private final JPanel pnlSouth = new JPanel();
	private final JButton btnExecute = new JButton("Executar");
	private final PropertiesManager propertiesManager = new PropertiesManager();

	private final ActionListener cbxCommandsActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			pnlParameters.removeAll();
			validate();
			repaint();
			enableDisableBtnAddParemeter();
		}
	};
	private final ActionListener btnAddParemeterActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			pnlParameters.add(new PnlParameter(getCurrentLiquibaseCommand(), pnlParameters, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enableDisableBtnAddParemeter();
				}
			}));
			
			enableDisableBtnAddParemeter();
		}

	};

	private final ActionListener btnExecuteActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			List<String> parameters = new ArrayList<String>();
			List<String> parametersValues = new ArrayList<String>();

			Component[] components = pnlParameters.getComponents();
			PnlParameter pnlParameter;
			for (Component component : components) {
				pnlParameter = (PnlParameter) component;
				parameters.add(pnlParameter.getSelectedParemeter());
				parametersValues.add(pnlParameter.getParameterValue());
			}

			StringBuilder sb = new StringBuilder(Utils.getJARDir() + File.separator + LIQUIBASE_EXECUTABLE);
			String command = getCurrentLiquibaseCommand().getCommand();
			sb.append(command);

			if (cbxCommands.getSelectedItem().equals(LiquibaseCommand.updateSQL)) {
				sb.append(" > " + parametersValues.get(0));
			} else if (cbxCommands.getSelectedItem().equals(LiquibaseCommand.dbDoc)) {
				sb.append(" " + parametersValues.get(0));
			} else {
				for (int i = 0; i < parameters.size(); i++) {
					sb.append(" --" + parameters.get(i) + "="
							+ parametersValues.get(i));
				}
			}

			System.out.println(sb.toString());
			final LiquiguiCommandExecutor frame = new LiquiguiCommandExecutor();
			frame.setVisible(true);
			saveProperties();
			frame.executeCommand(sb.toString());
		}
	};
	private final JPanel panel = new JPanel();
	private final JLabel lblNewLabel = new JLabel("Driver");
	private final JTextField txtDriver = new JTextField();
	private final JLabel lblNewLabel_1 = new JLabel("URL");
	private final JTextField txtUrl = new JTextField();
	private final JLabel lblUsername = new JLabel("Username");
	private final JTextField txtUsernae = new JTextField();
	private final JLabel lblPassword = new JLabel("Password");
	private final JTextField txtPassword = new JTextField();
	private final JLabel lblChangelogfile = new JLabel("changeLogFile");
	private final JTextField txtChangeLogFile = new JTextField();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Liquigui frame = new Liquigui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Liquigui() {
		txtUrl.setColumns(10);
		txtDriver.setColumns(10);
		buildGUI();
	}

	private void buildGUI() {
		//Configura janela
		setBounds(100, 100, 523, 309);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		//Centraliza Label 'Liquigui' e configura fonte
		lblLiquigui.setHorizontalAlignment(SwingConstants.CENTER);
		lblLiquigui.setFont(new Font("Tahoma", Font.BOLD, 20));

		//Setta layout dos paineis
		contentPane.setLayout(new BorderLayout(0, 0));
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
		pnlCommand.setLayout(new BoxLayout(pnlCommand, BoxLayout.X_AXIS));
		pnlParameters.setLayout(new BoxLayout(pnlParameters, BoxLayout.Y_AXIS));

		//Seta tamanho maximo para painel de parametros
		pnlCommand.setMaximumSize(new Dimension(800, 20));

		//Retorna comandos disponiveis, sem o DEFAULT
		List<LiquibaseCommand> commands = new ArrayList<LiquibaseCommand>(Arrays.asList(LiquibaseCommand.values()));
		commands.remove(LiquibaseCommand.DEFAULT);

		//Alimenta combobox
		cbxCommands.setModel(new DefaultComboBoxModel<LiquibaseCommand>(commands.toArray(new LiquibaseCommand[] {})));

		//Alinha botao de 'Executar' a direita
		((FlowLayout)pnlSouth.getLayout()).setAlignment(FlowLayout.RIGHT);

		//Seta listeners
		cbxCommands.addActionListener(cbxCommandsActionListener);
		btnAddParemeter.addActionListener(btnAddParemeterActionListener);
		btnExecute.addActionListener(btnExecuteActionListener);

		//Adiciona paineis uns aos outros
		setContentPane(contentPane);
		contentPane.add(lblLiquigui, BorderLayout.NORTH);
		contentPane.add(pnlCenter);
		contentPane.add(pnlSouth, BorderLayout.SOUTH);
		pnlCenter.add(panel);
		panel.setLayout(new GridLayout(5, 2, 0, 0));
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		panel.add(lblNewLabel);
		
		panel.add(txtDriver);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		
		panel.add(lblNewLabel_1);
		
		panel.add(txtUrl);
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		
		panel.add(lblUsername);
		txtUsernae.setColumns(10);
		
		panel.add(txtUsernae);
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		
		panel.add(lblPassword);
		txtPassword.setColumns(10);
		
		panel.add(txtPassword);
		lblChangelogfile.setHorizontalAlignment(SwingConstants.LEFT);
		
		panel.add(lblChangelogfile);
		txtChangeLogFile.setColumns(10);
		
		panel.add(txtChangeLogFile);
		pnlCenter.add(pnlCommand);
		pnlCommand.add(lblEscolhaOComando);
		pnlCommand.add(cbxCommands);
		pnlCommand.add(btnAddParemeter);
		pnlCenter.add(pnlParameters);
		pnlSouth.add(btnExecute, BorderLayout.CENTER);
		
		loadProperties();
		
		enableDisableBtnAddParemeter();
	}

	private void loadProperties() {
		txtChangeLogFile.setText(propertiesManager.getChangeLogFile());
		txtDriver.setText(propertiesManager.getDriver());
		txtPassword.setText(propertiesManager.getPassword());
		txtUsernae.setText(propertiesManager.getUsername());
		txtUrl.setText(propertiesManager.getUrl());
	}
	private void saveProperties() {
		propertiesManager.setChangeLogFile(txtChangeLogFile.getText());
		propertiesManager.setDriver(txtDriver.getText());
		propertiesManager.setPassword(txtPassword.getText());
		propertiesManager.setUsername(txtUsernae.getText());
		propertiesManager.setUrl(txtUrl.getText());
		propertiesManager.save();
	}
	
	private boolean canAddParemeter(){
		int params = getCurrentLiquibaseCommand().getParams().length;
		int components = pnlParameters.getComponents().length;
		
		if(params == 1 && getCurrentLiquibaseCommand().getParams()[0].isEmpty())
			return false;
		
		return components < params;
	}
	private void enableDisableBtnAddParemeter(){
		btnAddParemeter.show(canAddParemeter());
		validate();
		repaint();
	}
	private LiquibaseCommand getCurrentLiquibaseCommand() {
		LiquibaseCommand liquibaseCommand = (LiquibaseCommand) cbxCommands.getSelectedItem();
		return liquibaseCommand;
	}

}
