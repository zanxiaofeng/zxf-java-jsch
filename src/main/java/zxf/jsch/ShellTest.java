package zxf.jsch;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class ShellTest {
    public static void main(String[] args) throws JSchException, IOException, SftpException, InterruptedException {
        testUsernameAndPassword();
        testUsernameAndIdentity();
    }

    private static void testUsernameAndPassword() throws JSchException, IOException, SftpException, InterruptedException {
        System.out.println("*****************************testUsernameAndPassword*****************************");
        Session session = JschFactory.createSession("sftp-user", "passwd", "localhost", 2222);
        ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
        PipedInputStream in = new PipedInputStream();
        channelShell.setInputStream(in);
        channelShell.setOutputStream(System.out, true);
        channelShell.connect();
        PipedOutputStream out = new PipedOutputStream(in);
        out.write("cat /proc/version\n".getBytes(StandardCharsets.UTF_8));
        out.flush();
        Thread.sleep(12000);
        channelShell.disconnect();
        session.disconnect();
    }

    private static void testUsernameAndIdentity() throws JSchException, IOException, SftpException, InterruptedException {
        System.out.println("*****************************testUsernameAndIdentity*****************************");
        Session session = JschFactory.createSession("sftp-user", Paths.get("./src/main/resources/keystore/my-sshkey"), "localhost", 2222);
        ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
        PipedInputStream in = new PipedInputStream();
        channelShell.setInputStream(in);
        channelShell.setOutputStream(System.out, true);
        channelShell.connect();
        PipedOutputStream out = new PipedOutputStream(in);
        out.write("cat /proc/version\n".getBytes(StandardCharsets.UTF_8));
        out.flush();
        Thread.sleep(12000);
        channelShell.disconnect();
        session.disconnect();
    }
}
