package br.com.cseabra.liquigui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesManager {
	private static final String LIQUIBASE_PROPERTIES = "liquibase.properties";
	private String driver, username, password, url, changeLogFile;
	
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getChangeLogFile() {
		return changeLogFile;
	}

	public void setChangeLogFile(String changeLogFile) {
		this.changeLogFile = changeLogFile;
	}

	public PropertiesManager(){
		loadProperties();
	}

	private void loadProperties() {
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
	 
			input = new FileInputStream(LIQUIBASE_PROPERTIES);
	 
			// load a properties file
			prop.load(input);
			setProperties(prop);
		} catch (IOException ex) {
			setProperties(prop);
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void setProperties(Properties prop) {
		setDriver(prop.getProperty("driver", DatabaseType.ORACLE.getDriver()));
		setUsername(prop.getProperty("username", ""));
		setPassword(prop.getProperty("password", ""));
		setUrl(prop.getProperty("url", ""));
		setChangeLogFile(prop.getProperty("changeLogFile", ""));
	}
	
	public void save(){
		Properties prop = new Properties();
		OutputStream output = null;
	 
		try {
	 
			output = new FileOutputStream(LIQUIBASE_PROPERTIES);
	 
			prop.setProperty("driver", getDriver());
			prop.setProperty("username", getUsername());
			prop.setProperty("password", getPassword());
			prop.setProperty("url", getUrl());
			prop.setProperty("changeLogFile", getChangeLogFile());
	 
			prop.store(output, null);
	 
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	 
		}
	}
}
