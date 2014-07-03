package br.com.cseabra.liquigui.liquibase;

public enum Commands {
	generateChangeLog {
		private String[] params = new String[] { "" };

		@Override
		public String[] getParams() {
			return params;
		}
	},
	migrate {
		private String[] params = new String[] { "classpath" };

		@Override
		public String[] getParams() {
			return params;
		}
	},
	dbDoc {
		private String[] params = new String[] { "outputDirectory" };

		@Override
		public String[] getParams() {
			return params;
		}
	},
	updateSQL {
		private String[] params = new String[] { "outputFile" };

		@Override
		public String[] getParams() {
			return params;
		}
	},
	DEFAULT {
		@Override
		public String[] getParams() {
			return new String[] { "" };
		}
	};

	public abstract String[] getParams();
}
