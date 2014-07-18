package br.com.cseabra.liquigui;

public enum DatabaseType {
	ORACLE {
		@Override
		String getDriver() {
			return "oracle.jdbc.OracleDriver";
		}

		@Override
		String getURL() {
			return "jdbc:oracle:thin:@<LOCAL>:1521:<SCHEMA>"; 
		}
	}, SQLSERVER {
		@Override
		String getDriver() {
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}

		@Override
		String getURL() {
			return "jdbc:sqlserver://<LOCAL>:1433;databaseName=<SCHEMA>";
		}
	}, MYSQL {
		@Override
		String getDriver() {
			return "org.hibernate.dialect.MySQLDialect";
		}

		@Override
		String getURL() {
			return "jdbc:mysql://<LOCAL>:3306/<SCHEMA>";
		}
	}, H2 {
		@Override
		String getDriver() {
			return "org.h2.Driver";
		}

		@Override
		String getURL() {
			return "jdbc:h2:file:<LOCAL>;DATABASE_TO_UPPER=false;ALIAS_COLUMN_NAME=true";
		}
	};
	abstract String getDriver();
	abstract String getURL();
	
	public static class Utils {
		public static DatabaseType getByDriver(String driver){
			for(DatabaseType dbType : DatabaseType.values()){
				if(dbType.getDriver().equals(driver)) return dbType;
			}
			return null;
		}
	}
}
