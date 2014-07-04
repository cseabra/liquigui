package br.com.cseabra.liquigui;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;

public class Utils {
	public static String getJARDir() {
		CodeSource codeSource = Utils.class.getProtectionDomain()
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
