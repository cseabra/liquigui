package br.com.cseabra.liquigui;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class LiquiguiCommandExecutor extends JFrame {
	private static final long serialVersionUID = 1L;

	class RuntimeExec {
		public StreamWrapper getStreamWrapper(InputStream is) {
			return new StreamWrapper(is);
		}

		private class StreamWrapper extends Thread {
			InputStream is = null;

			StreamWrapper(InputStream is) {
				this.is = is;
			}

			public void run() {
				try {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is));
					String line = null;
					while ((line = br.readLine()) != null) {
						textArea.append(line);
						textArea.append("\n");
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}

	private JPanel contentPane;
	private final JTextArea textArea = new JTextArea();

	/**
	 * Create the frame.
	 */
	public LiquiguiCommandExecutor() {
		setTitle("Executando comando ...");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		textArea.setLineWrap(true);
		contentPane.add(textArea);
	}

	public void executeCommand(final String command) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				Runtime rt = Runtime.getRuntime();
				RuntimeExec rte = new RuntimeExec();
				Process proc = null;
				try {
					proc = rt.exec(command);

					RuntimeExec.StreamWrapper error, output;

					error = rte.getStreamWrapper(proc.getErrorStream());
					output = rte.getStreamWrapper(proc.getInputStream());
					error.start();
					output.start();

					proc.waitFor();
				} catch (Exception e) {
					textArea.append("Oops, algum erro ocorreu: "+ e.getMessage() +"\n\n");
					for(StackTraceElement trace : e.getStackTrace()){
						textArea.append("   " + trace.toString() + "\n");
					}
				}
				setTitle("Comando executado");
			}
		});
		t.start();
	}
}
