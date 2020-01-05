package com.ita07.autoclicker.classes;

/*This class only exists so that we can produce a fat/uber jar. Otherwise, if we directly run from Main it will fail due to
an error from  sun.launcher.LauncherHelper.
*/
public class MainLauncher {

    public static void main(String[] args) {
        Main.main(args);
    }
}
