package zxf.jsch;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class SftpTests {
    public static void main(String[] args) throws JSchException, IOException, SftpException {
        testUsernameAndPassword();
        testUsernameAndIdentity();
    }

    private static void testUsernameAndPassword() throws JSchException, IOException, SftpException {
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

    private static void testUsernameAndIdentity() throws JSchException, IOException, SftpException {
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
}
