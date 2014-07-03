package br.com.cseabra.liquigui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import br.com.cseabra.liquigui.liquibase.Commands;

public class Liquigui extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPane = new JPanel();
	private final JLabel lblLiquigui = new JLabel("Liquigui");
	private final JPanel pnlCenter = new JPanel();
	private final JPanel pnlCommand = new JPanel();
	private final JLabel lblEscolhaOComando = new JLabel("Escolha o comando");
	private final JPanel pnlParameters = new JPanel();
	private final JComboBox<Commands> cbxCommands = new JComboBox<Commands>();
	private final JButton btnAddParemeter = new JButton("Adicionar parâmetro");
	private final JPanel pnlSouth = new JPanel();
	private final JButton btnExecute = new JButton("Executar");

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

	/**
	 * Create the frame.
	 */
	public Liquigui() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		lblLiquigui.setHorizontalAlignment(SwingConstants.CENTER);
		lblLiquigui.setFont(new Font("Tahoma", Font.BOLD, 20));
		contentPane.add(lblLiquigui, BorderLayout.NORTH);

		contentPane.add(pnlCenter);
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));

		pnlCenter.add(pnlCommand);
		pnlCommand.setLayout(new BoxLayout(pnlCommand, BoxLayout.X_AXIS));

		pnlCommand.add(lblEscolhaOComando);

		cbxCommands.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pnlParameters.removeAll();
				validate();
				repaint();
			}
		});
		pnlCommand.add(cbxCommands);

		// Remove DEFAULT da lista de comandos
		List<Commands> commands = new ArrayList<Commands>(
				Arrays.asList(Commands.values()));
		commands.remove(Commands.DEFAULT);

		pnlCommand.setMaximumSize(new Dimension(800, 20));
		cbxCommands.setModel(new DefaultComboBoxModel<Commands>(commands
				.toArray(new Commands[] {})));

		pnlCommand.add(btnAddParemeter);
		btnAddParemeter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pnlParameters.add(new PnlParameter((Commands) cbxCommands
						.getSelectedItem()));
				validate();
				repaint();
			}
		});

		pnlCenter.add(pnlParameters);
		pnlParameters.setLayout(new BoxLayout(pnlParameters, BoxLayout.Y_AXIS));

		FlowLayout fl_pnlSouth = (FlowLayout) pnlSouth.getLayout();
		fl_pnlSouth.setAlignment(FlowLayout.RIGHT);
		contentPane.add(pnlSouth, BorderLayout.SOUTH);
		btnExecute.addActionListener(new ActionListener() {
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

				StringBuilder sb = new StringBuilder(getJARDir()
						+ File.separator + "liquibase.bat ");
				String command = cbxCommands.getSelectedItem().toString();
				sb.append(command);

				if (cbxCommands.getSelectedItem().equals(Commands.updateSQL)) {
					sb.append(" > " + parametersValues.get(0));
				} else if(cbxCommands.getSelectedItem().equals(Commands.dbDoc)) {
					sb.append(" " + parametersValues.get(0));
				}else {
					for (int i = 0; i < parameters.size(); i++) {
						sb.append(" --" + parameters.get(i) + "=" + parametersValues.get(i));
					}
				}

				System.out.println(sb.toString());
				final LiquiguiCommandExecutor frame = new LiquiguiCommandExecutor();
				frame.setVisible(true);
				frame.executeCommand(sb.toString());
			}
		});

		pnlSouth.add(btnExecute, BorderLayout.CENTER);
	}

	private static String getJARDir() {
		CodeSource codeSource = Liquigui.class.getProtectionDomain()
				.getCodeSource();
		File jarFile;
		try {
			jarFile = new File(codeSource.getLocation().toURI().getPath());
			return jarFile.getParentFile().getPath();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
