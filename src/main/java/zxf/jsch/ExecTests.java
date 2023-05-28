package zxf.jsch;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.nio.file.Paths;

public class ExecTests {
    public static void main(String[] args) throws JSchException, IOException, SftpException, InterruptedException {
        testUsernameAndPassword();
        testUsernameAndIdentity();
    }

    private static void testUsernameAndPassword() throws JSchException, IOException, SftpException, InterruptedException {
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

    private static void testUsernameAndIdentity() throws JSchException, IOException, SftpException, InterruptedException {
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
}
