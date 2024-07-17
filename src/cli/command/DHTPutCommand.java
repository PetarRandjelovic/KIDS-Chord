package cli.command;

import app.AppConfig;
import app.ChordState;

import java.io.File;

public class DHTPutCommand implements CLICommand {

	@Override
	public String commandName() {
		return "dht_put";
	}

	@Override
	public void execute(String args) {
		String[] splitArgs = args.split(" ");
		
		if (splitArgs.length == 2) {

			int key = 0;
			int value = 0;

				key = Integer.parseInt(splitArgs[0]);

				if(isInteger(splitArgs[1])) {
					value = Integer.parseInt(splitArgs[1]);

					if (key < 0 || key >= ChordState.CHORD_SIZE) {
						throw new NumberFormatException();
					}
					if (value < 0) {
						throw new NumberFormatException();
					}
					AppConfig.chordState.putValue(key, value);
				} else{
					String valueString = splitArgs[1];
					if (key < 0 || key >= ChordState.CHORD_SIZE) {
						throw new NumberFormatException();
					}
					File file = new File("rootFolder//"+valueString);
					AppConfig.chordState.putValueFile(key, file,"public");
				}



		} else {
			AppConfig.timestampedErrorPrint("Invalid arguments for put");
		}

	}

	public boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
