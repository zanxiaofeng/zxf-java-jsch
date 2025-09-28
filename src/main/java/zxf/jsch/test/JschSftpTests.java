package zxf.jsch.test;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import zxf.jsch.JschFactory;

import java.io.ByteArrayInputStream;
import java.nio.file.Paths;

public class JschSftpTests {
    public static void main(String[] args) throws Exception {
        testUsernameAndPassword();
        testUsernameAndIdentityFromSSH();
        testUsernameAndIdentityFromJKS();
    }

    private static void testUsernameAndPassword() throws Exception {
        System.out.println(Thread.currentThread() + " *****************************testUsernameAndPassword*****************************");
        Session session = JschFactory.createSession("sftp-user", "passwd", "localhost", 2222);
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        channelSftp.put(new ByteArrayInputStream("Hello 1".getBytes()), "/upload/hello-test1.txt");
        channelSftp.chmod(0666, "/upload/hello-test1.txt");
        channelSftp.ls("/upload").forEach(System.out::println);
        channelSftp.disconnect();
        session.disconnect();
    }

    private static void testUsernameAndIdentityFromSSH() throws Exception {
        System.out.println(Thread.currentThread() + " *****************************testUsernameAndIdentity*****************************");
        Session session = JschFactory.createSession("sftp-user", Paths.get("./src/main/resources/keystore/my-sshkey"), "localhost", 2222);
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        channelSftp.put(new ByteArrayInputStream("Hello 2".getBytes()), "/upload/hello-test2.txt");
        channelSftp.chmod(0666, "/upload/hello-test2.txt");
        channelSftp.ls("/upload").forEach(System.out::println);
        channelSftp.disconnect();
        session.disconnect();
    }

    private static void testUsernameAndIdentityFromJKS() throws Exception {
        System.out.println(Thread.currentThread() + " *****************************testUsernameAndIdentity*****************************");
        Session session = JschFactory.createSession("sftp-user", "./src/main/resources/keystore/mykeystore.jks", "changeit", "myPrivateKey", "localhost", 2222);
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        channelSftp.put(new ByteArrayInputStream("Hello 2".getBytes()), "/upload/hello-test2.txt");
        channelSftp.chmod(0666, "/upload/hello-test2.txt");
        channelSftp.ls("/upload").forEach(System.out::println);
        channelSftp.disconnect();
        session.disconnect();
    }
}
