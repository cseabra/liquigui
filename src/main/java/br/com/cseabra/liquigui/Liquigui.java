package br.com.cseabra.liquigui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import br.com.cseabra.liquigui.liquibase.LiquibaseCommand;
import br.com.cseabra.liquigui.liquibase.LogLevel;

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
	
	private final ActionListener cbxDatabasesListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			changeSchemaValue();
		}
	};
	private final KeyListener txtSchemaKeyListener = new KeyListener() {
		public void keyPressed(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {
			changeSchemaValue();
		}
		public void keyTyped(KeyEvent e) {}
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
	private final JComboBox<DatabaseType> cbxDatabases = new JComboBox<DatabaseType>();
	private final JLabel lblNewLabel_1 = new JLabel("URL");
	private final JTextField txtUrl = new JTextField();
	private final JLabel lblUsername = new JLabel("Username");
	private final JTextField txtUsernae = new JTextField();
	private final JLabel lblPassword = new JLabel("Password");
	private final JTextField txtPassword = new JTextField();
	private final JLabel lblChangelogfile = new JLabel("changeLogFile");
	private final JTextField txtChangeLogFile = new JTextField();
	private final JLabel lblSchema = new JLabel("Schema");
	private final JTextField txtSchema = new JTextField();
	private final JComboBox<String> cbxLogLevel = new JComboBox<String>();
	private final JLabel lblNvelDoLog = new JLabel("Nível do log");
	private final JLabel lblNewLabel_2 = new JLabel("Local");
	private final JTextField txtLocal = new JTextField();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Liquigui frame = new Liquigui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Liquigui() {
		setResizable(false);
		buildGUI();
	}

	private void buildGUI() {
		//Configura janela
		setBounds(100, 100, 523, 348);
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

		//Alimenta comboboxs
		cbxCommands.setModel(new DefaultComboBoxModel<LiquibaseCommand>(commands.toArray(new LiquibaseCommand[] {})));
		
		{
			String[] logLevels = new String[LogLevel.values().length];
			for(int i = 0; i < LogLevel.values().length; i++){
				logLevels[i] = LogLevel.values()[i].toString();
			}
			cbxLogLevel.setModel(new DefaultComboBoxModel<String>(logLevels));
		}
		//Alinha botao de 'Executar' a direita
		((FlowLayout)pnlSouth.getLayout()).setAlignment(FlowLayout.RIGHT);

		//Seta listeners
		cbxCommands.addActionListener(cbxCommandsActionListener);
		btnAddParemeter.addActionListener(btnAddParemeterActionListener);
		btnExecute.addActionListener(btnExecuteActionListener);
		txtLocal.setText("localhost");
		txtLocal.addKeyListener(txtSchemaKeyListener);

		//Adiciona paineis uns aos outros
		setContentPane(contentPane);
		contentPane.add(lblLiquigui, BorderLayout.NORTH);
		contentPane.add(pnlCenter);
		FlowLayout flowLayout = (FlowLayout) pnlSouth.getLayout();
		flowLayout.setVgap(10);
		contentPane.add(pnlSouth, BorderLayout.SOUTH);
		pnlCenter.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {75, 430};
		gbl_panel.rowHeights = new int[] {30, 30, 30, 30, 30, 30, 25};
		gbl_panel.columnWeights = new double[]{0.0, 0.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(gbl_panel);
		cbxDatabases.setModel(new DefaultComboBoxModel<DatabaseType>(DatabaseType.values()));
		cbxDatabases.addActionListener(cbxDatabasesListener);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		GridBagConstraints gbc_cbxDatabases = new GridBagConstraints();
		gbc_cbxDatabases.fill = GridBagConstraints.BOTH;
		gbc_cbxDatabases.insets = new Insets(0, 0, 5, 0);
		gbc_cbxDatabases.gridx = 1;
		gbc_cbxDatabases.gridy = 0;
		panel.add(cbxDatabases, gbc_cbxDatabases);
		GridBagConstraints gbc_lblSchema = new GridBagConstraints();
		gbc_lblSchema.fill = GridBagConstraints.BOTH;
		gbc_lblSchema.insets = new Insets(0, 0, 5, 5);
		gbc_lblSchema.gridx = 0;
		gbc_lblSchema.gridy = 1;
		panel.add(lblSchema, gbc_lblSchema);
		txtSchema.addKeyListener(txtSchemaKeyListener);
		GridBagConstraints gbc_txtSchema = new GridBagConstraints();
		gbc_txtSchema.fill = GridBagConstraints.BOTH;
		gbc_txtSchema.insets = new Insets(0, 0, 5, 0);
		gbc_txtSchema.gridx = 1;
		gbc_txtSchema.gridy = 1;
		panel.add(txtSchema, gbc_txtSchema);
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.fill = GridBagConstraints.BOTH;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 2;
		panel.add(lblUsername, gbc_lblUsername);
		txtUsernae.setColumns(10);
		GridBagConstraints gbc_txtUsernae = new GridBagConstraints();
		gbc_txtUsernae.fill = GridBagConstraints.BOTH;
		gbc_txtUsernae.insets = new Insets(0, 0, 5, 0);
		gbc_txtUsernae.gridx = 1;
		gbc_txtUsernae.gridy = 2;
		panel.add(txtUsernae, gbc_txtUsernae);
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.fill = GridBagConstraints.BOTH;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 3;
		panel.add(lblPassword, gbc_lblPassword);
		txtPassword.setColumns(10);
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.fill = GridBagConstraints.BOTH;
		gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtPassword.gridx = 1;
		gbc_txtPassword.gridy = 3;
		panel.add(txtPassword, gbc_txtPassword);
		lblChangelogfile.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblChangelogfile = new GridBagConstraints();
		gbc_lblChangelogfile.fill = GridBagConstraints.BOTH;
		gbc_lblChangelogfile.insets = new Insets(0, 0, 5, 5);
		gbc_lblChangelogfile.gridx = 0;
		gbc_lblChangelogfile.gridy = 4;
		panel.add(lblChangelogfile, gbc_lblChangelogfile);
		txtChangeLogFile.setColumns(10);
		GridBagConstraints gbc_txtChangeLogFile = new GridBagConstraints();
		gbc_txtChangeLogFile.fill = GridBagConstraints.BOTH;
		gbc_txtChangeLogFile.insets = new Insets(0, 0, 5, 0);
		gbc_txtChangeLogFile.gridx = 1;
		gbc_txtChangeLogFile.gridy = 4;
		panel.add(txtChangeLogFile, gbc_txtChangeLogFile);
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 5;
		panel.add(lblNewLabel_2, gbc_lblNewLabel_2);
		GridBagConstraints gbc_txtLocal = new GridBagConstraints();
		gbc_txtLocal.fill = GridBagConstraints.BOTH;
		gbc_txtLocal.insets = new Insets(0, 0, 5, 0);
		gbc_txtLocal.gridx = 1;
		gbc_txtLocal.gridy = 5;
		panel.add(txtLocal, gbc_txtLocal);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 6;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_txtUrl = new GridBagConstraints();
		gbc_txtUrl.fill = GridBagConstraints.BOTH;
		gbc_txtUrl.gridx = 1;
		gbc_txtUrl.gridy = 6;
		panel.add(txtUrl, gbc_txtUrl);
		pnlCenter.add(pnlCommand);
		pnlCommand.add(lblEscolhaOComando);
		pnlCommand.add(cbxCommands);
		pnlCommand.add(btnAddParemeter);
		pnlCenter.add(pnlParameters);
		
		pnlSouth.add(lblNvelDoLog);
		
		pnlSouth.add(cbxLogLevel);
		pnlSouth.add(btnExecute, BorderLayout.CENTER);
		
		
		loadProperties();
		
		enableDisableBtnAddParemeter();
	}

	private void loadProperties() {
		txtChangeLogFile.setText(propertiesManager.getChangeLogFile());
		cbxDatabases.setSelectedItem( DatabaseType.Utils.getByDriver( propertiesManager.getDriver() ));
		txtPassword.setText(propertiesManager.getPassword());
		txtUsernae.setText(propertiesManager.getUsername());
		txtSchema.setText(propertiesManager.getDefaultSchemaName());
		txtLocal.setText(propertiesManager.getLocal());
		cbxLogLevel.setSelectedItem(propertiesManager.getLogLevel());
		if(propertiesManager.getUrl() == null || propertiesManager.getUrl().isEmpty()){
			changeSchemaValue();
		} else {
			txtUrl.setText(propertiesManager.getUrl());
		}
	}
	private void saveProperties() {
		propertiesManager.setChangeLogFile(txtChangeLogFile.getText());
		propertiesManager.setDriver(cbxDatabases.getItemAt(cbxDatabases.getSelectedIndex()).getDriver());
		propertiesManager.setPassword(txtPassword.getText());
		propertiesManager.setUsername(txtUsernae.getText());
		propertiesManager.setUrl(txtUrl.getText());
		propertiesManager.setDefaultSchemaName(txtSchema.getText());
		propertiesManager.setLocal(txtLocal.getText());
		propertiesManager.setLogLevel(cbxLogLevel.getSelectedItem().toString());
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

	private void changeSchemaValue() {
		StringBuilder url = new StringBuilder(cbxDatabases.getItemAt(cbxDatabases.getSelectedIndex()).getURL());

		int has;
		
		has = url.indexOf("<LOCAL>");
		if(!txtLocal.getText().isEmpty() && has > -1)
			url.replace(has, "<LOCAL>".length() + has,  txtLocal.getText());
		
		has = url.indexOf("<SCHEMA>");
		if(!txtSchema.getText().isEmpty() && has > -1)
			url.replace(has, "<SCHEMA>".length() + has,  txtSchema.getText());
		
		txtUrl.setText(url.toString());
	}
}
