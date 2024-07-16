package zxf.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.nio.file.Path;

public class JschFactory {
    static {
        JSch.setLogger(new Logger() {
            @Override
            public boolean isEnabled(int i) {
                return true;
            }

            @Override
            public void log(int i, String s) {
                System.out.println(Thread.currentThread() + " jsch:: " + s);
            }
        });
    }

    public static Session createSession(String username, String passwd, String host, int port) throws JSchException, IOException {
        System.out.println(Thread.currentThread() + " JschFactory::createSession");
        JSch jSch = new JSch();
        Session session = jSch.getSession(username, host, port);
        session.setPassword(passwd);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PreferredAuthentications", "password");
        session.setServerAliveInterval(120);
        session.setTimeout(3000);
        session.connect();
        return session;
    }

    public static Session createSession(String username, Path identity, String host, int port) throws JSchException, IOException {
        System.out.println(Thread.currentThread() + " JschFactory::createSession");
        JSch jSch = new JSch();
        jSch.addIdentity(identity.toString());
        Session session = jSch.getSession(username, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PreferredAuthentications", "publickey");
        session.setServerAliveInterval(120);
        session.setTimeout(3000);
        session.connect();
        return session;
    }
}
