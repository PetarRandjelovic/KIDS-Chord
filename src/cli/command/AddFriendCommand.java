package cli.command;

import app.AppConfig;

import java.io.File;


public class AddFriendCommand implements CLICommand{



    @Override
    public String commandName() {
        return "add_friend";
    }

    @Override
    public void execute(String args) {

        String[] splitArgs = args.split(":");

        if (splitArgs.length == 2) {

            AppConfig.timestampedErrorPrint(splitArgs[0]+" "+splitArgs[1]);

            AppConfig.myServentInfo.friendList.add(splitArgs[0]+":"+splitArgs[1]);

            AppConfig.timestampedErrorPrint("Friend added successfully "+AppConfig.myServentInfo.friendList);
        }

    }
}
