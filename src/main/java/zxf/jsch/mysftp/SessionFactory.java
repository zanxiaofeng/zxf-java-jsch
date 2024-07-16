package zxf.jsch.mysftp;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;
import lombok.AllArgsConstructor;

import java.nio.file.Path;

@AllArgsConstructor
public class SessionFactory {
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

    private String username;
    private Path identity;
    private String host;
    private int port;

    public Session createSession() throws JSchException {
        System.out.println(Thread.currentThread() + " SessionFactory::createSession");
        JSch jSch = new JSch();
        jSch.addIdentity(identity.toString());
        Session session = jSch.getSession(username, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PreferredAuthentications", "publickey");
        session.setServerAliveInterval(120);
        session.setTimeout(3000);
        return session;
    }
}
