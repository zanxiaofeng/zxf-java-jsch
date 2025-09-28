package zxf.jsch.test;

import com.jcraft.jsch.*;
import zxf.jsch.JschFactory;
import java.nio.file.Paths;

public class JschExecTests {
    public static void main(String[] args) throws Exception {
        testUsernameAndPassword();
        testUsernameAndIdentityFromSSH();
        testUsernameAndIdentityFromJKS();
    }

    private static void testUsernameAndPassword() throws Exception {
        System.out.println(Thread.currentThread() + " *****************************testUsernameAndPassword*****************************");
        Session session = JschFactory.createSession("sftp-user", "passwd", "localhost", 2222);
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setOutputStream(System.out, true);
        channelExec.setCommand("cat /proc/version");
        channelExec.connect();
        Thread.sleep(12000);
        channelExec.disconnect();
        session.disconnect();
    }

    private static void testUsernameAndIdentityFromSSH() throws Exception {
        System.out.println(Thread.currentThread() + " *****************************testUsernameAndIdentity*****************************");
        Session session = JschFactory.createSession("sftp-user", Paths.get("./src/main/resources/keystore/my-sshkey"), "localhost", 2222);
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setOutputStream(System.out, true);
        channelExec.setCommand("cat /proc/version");
        channelExec.connect();
        Thread.sleep(12000);
        channelExec.disconnect();
        session.disconnect();
    }

    private static void testUsernameAndIdentityFromJKS() throws Exception {
        System.out.println(Thread.currentThread() + " *****************************testUsernameAndIdentity*****************************");
        Session session = JschFactory.createSession("sftp-user", "./src/main/resources/keystore/mykeystore.jks", "changeit", "myPrivateKey", "localhost", 2222);
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setOutputStream(System.out, true);
        channelExec.setCommand("cat /proc/version");
        channelExec.connect();
        Thread.sleep(12000);
        channelExec.disconnect();
        session.disconnect();
    }
}
