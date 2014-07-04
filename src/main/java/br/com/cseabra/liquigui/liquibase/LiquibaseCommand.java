package br.com.cseabra.liquigui.liquibase;

public enum LiquibaseCommand {
	generateChangeLog {
		private String[] params = new String[] { "" };

		@Override
		public String[] getParams() {
			return params;
		}

		@Override
		public String getDescription() {
			return "Gerar changelog";
		}

		@Override
		public String getCommand() {
			return "generateChangeLog";
		}
	},
	migrate {
		private String[] params = new String[] { "classpath" };

		@Override
		public String[] getParams() {
			return params;
		}

		@Override
		public String getDescription() {
			return "Atualizar base";
		}

		@Override
		public String getCommand() {
			return "migrate";
		}
	},
	dbDoc {
		private String[] params = new String[] { "outputDirectory" };

		@Override
		public String[] getParams() {
			return params;
		}

		@Override
		public String getDescription() {
			return "Gerar relatório";
		}

		@Override
		public String getCommand() {
			return "dbDoc";
		}
	},
	updateSQL {
		private String[] params = new String[] { "outputFile" };

		@Override
		public String[] getParams() {
			return params;
		}

		@Override
		public String getDescription() {
			return "Gerar arquivo SQL";
		}

		@Override
		public String getCommand() {
			return "updateSQL";
		}
	},
	DEFAULT {
		@Override
		public String[] getParams() {
			return new String[] { "" };
		}
		
		@Override
		public String getDescription() {
			return "";
		}

		@Override
		public String getCommand() {
			return "";
		}
	};

	public abstract String[] getParams();
	public abstract String getDescription();
	public abstract String getCommand();
	
	@Override
	public String toString() {
		return getDescription();
	}
}
