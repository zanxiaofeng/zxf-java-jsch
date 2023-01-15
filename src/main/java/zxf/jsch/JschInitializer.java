package zxf.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Logger;

public class JschInitializer {
    public static void init(){
        JSch.setLogger(new Logger() {
            @Override
            public boolean isEnabled(int i) {
                return true;
            }

            @Override
            public void log(int i, String s) {
                System.out.println(s);
            }
        });
    }
}
