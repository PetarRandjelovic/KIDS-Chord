package cli.command;

import app.AppConfig;
import app.PingPongThread;
import cli.CLIParser;
import servent.SimpleServentListener;

public class StopCommand implements CLICommand {

	private CLIParser parser;
	private SimpleServentListener listener;
	private PingPongThread pingPongThread;
	
	public StopCommand(CLIParser parser, SimpleServentListener listener, PingPongThread pingPongThread) {
		this.parser = parser;
		this.listener = listener;
		this.pingPongThread = pingPongThread;
	}
	public StopCommand(CLIParser parser, SimpleServentListener listener) {
		this.parser = parser;
		this.listener = listener;

	}
	
	@Override
	public String commandName() {
		return "stop";
	}

	@Override
	public void execute(String args) {
		AppConfig.timestampedStandardPrint("Stopping...");
		parser.stop();
		listener.stop();
		pingPongThread.stop();
	}

}
