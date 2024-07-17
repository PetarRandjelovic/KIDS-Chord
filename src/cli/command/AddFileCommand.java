package cli.command;

import app.AppConfig;
import app.ChordState;
import mutex.SuzukiKasami;

import java.io.File;
import java.lang.reflect.Field;

public class AddFileCommand implements CLICommand{



    @Override
    public String commandName() {
        return "add_file";
    }

    @Override
    public void execute(String args) {

        String[] splitArgs = args.split(" ");

        if (splitArgs.length == 2) {

            if(!splitArgs[1].equals("public") && !splitArgs[1].equals("private")){
                AppConfig.timestampedErrorPrint("Wrong argument for add_file command. Should be add_file path public/private.");
                return;
            }

            File file = new File("rootFolder//"+splitArgs[0]);

         //   SuzukiKasami.lock();

            if(splitArgs[1].equals("public")){
                AppConfig.chordState.publicFiles.add(file);
                AppConfig.chordState.putValueFile(ChordState.chordHash(splitArgs[0]), file,"public");
                AppConfig.timestampedStandardPrint("File added to public files. "+file.getName());
            }
            else{
                AppConfig.chordState.privateFiles.add(file);
                AppConfig.chordState.putValueFile(ChordState.chordHash(splitArgs[0]), file,"private");
                AppConfig.timestampedStandardPrint("File added to private files. "+file.getName());
            }

         //   SuzukiKasami.unlock();

        }





    }
}