package zxf.jsch;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.IOException;

public class SftpTest {
    public static void main(String[] args) throws JSchException, IOException, SftpException {
        Session session = JschFactory.createSession("", "localhost", 2222);
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.ls("/").forEach(System.out::println);
    }
}
