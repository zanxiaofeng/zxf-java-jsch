package zxf.jsch.test;

import com.jcraft.jsch.*;
import zxf.jsch.JschFactory;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class JschShellTests {
    public static void main(String[] args) throws Exception {
        testUsernameAndPassword();
        testUsernameAndIdentityFromSSH();
        testUsernameAndIdentityFromJKS();
    }

    private static void testUsernameAndPassword() throws Exception {
        System.out.println(Thread.currentThread() + " *****************************testUsernameAndPassword*****************************");
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

    private static void testUsernameAndIdentityFromSSH() throws Exception {
        System.out.println(Thread.currentThread() + " *****************************testUsernameAndIdentity*****************************");
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

    private static void testUsernameAndIdentityFromJKS() throws Exception {
        System.out.println(Thread.currentThread() + " *****************************testUsernameAndIdentity*****************************");
        Session session = JschFactory.createSession("sftp-user", "./src/main/resources/keystore/mykeystore.jks", "changeit", "myPrivateKey", "localhost", 2222);
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
