package cli.command;

import app.AppConfig;
import mutex.SuzukiKasami;

import java.io.File;

public class RemoveFileCommand implements CLICommand {
    @Override
    public String commandName() {
        return "remove_file";
    }

    @Override
    public void execute(String args) {


        File file = new File("rootFolder//" + args);

     //   SuzukiKasami.lock();

        AppConfig.timestampedErrorPrint("File to be removed: " + file.getName());
        for (File f : AppConfig.chordState.getValueMapFilePublic().values()) {
            if (f.getName().equals(file.getName())) {
                AppConfig.chordState.getValueMapFilePublic().remove(f.getName());
                AppConfig.timestampedErrorPrint("File removed from public files. " + file.getName());
                break;
            }

        }
        for (File f : AppConfig.chordState.getValueMapFilePrivate().values()) {
            if (f.getName().equals(file.getName())) {
                AppConfig.chordState.getValueMapFilePrivate().remove(f.getName());
                AppConfig.timestampedErrorPrint("File removed from public files. " + file.getName());
                break;
            }

        }
     //   SuzukiKasami.unlock();


    }
}
