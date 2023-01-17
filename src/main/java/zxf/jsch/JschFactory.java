package zxf.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JschFactory {
    static {
        JSch.setLogger(new Logger() {
            @Override
            public boolean isEnabled(int i) {
                return true;
            }

            @Override
            public void log(int i, String s) {
                System.out.println("jsch:: " + s);
            }
        });
    }

    public static Session createSession(String username, String host, int port) throws JSchException, IOException {
        JSch jSch = new JSch();
        jSch.addIdentity(new String(Files.readAllBytes(Paths.get("./src/main/resources/keystore/my-sshkey"))));
        Session session = jSch.getSession(username, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setTimeout(3000);
        session.connect();
        return session;
    }
}
