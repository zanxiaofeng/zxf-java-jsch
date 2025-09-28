package zxf.jsch.mysftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Vector;

@Data
@AllArgsConstructor
public class MySftp {
    private final Session session;
    private final String basePath;

    public void cd(String folder) throws SftpException, JSchException {
        System.out.println(Thread.currentThread() + " MySftp::cd, " + folder);
        ChannelSftp channelSftp = this.makeChannelSftp();
        try {
            channelSftp.cd(folder);
        } finally {
            channelSftp.disconnect();
        }
    }

    public Vector list(String path) throws SftpException, JSchException {
        System.out.println(Thread.currentThread() + " MySftp::list, " + basePath + path);
        ChannelSftp channelSftp = this.makeChannelSftp();
        try {
            return channelSftp.ls(basePath + path);
        } finally {
            channelSftp.disconnect();
        }
    }

    public void createFolder(String folder, int permissions) throws SftpException, JSchException {
        System.out.println(Thread.currentThread() + " MySftp::createFolder, " + basePath + folder);
        ChannelSftp channelSftp = this.makeChannelSftp();
        try {
            channelSftp.mkdir(basePath + folder);
            channelSftp.chmod(permissions, basePath + folder);
        } finally {
            channelSftp.disconnect();
        }
    }

    public void upload(String dst, byte[] content, int permissions) throws SftpException, JSchException {
        System.out.println(Thread.currentThread() + " MySftp::upload, " + basePath + dst);
        ChannelSftp channelSftp = this.makeChannelSftp();
        try {
            channelSftp.put(new ByteArrayInputStream(content), basePath + dst);
            channelSftp.chmod(permissions, basePath + dst);
        } finally {
            channelSftp.disconnect();
        }
    }

    public byte[] download(String src) throws SftpException, JSchException {
        System.out.println(Thread.currentThread() + " MySftp::download, " + basePath + src);
        ChannelSftp channelSftp = this.makeChannelSftp();
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            channelSftp.get(basePath + src, outputStream);
            return outputStream.toByteArray();
        } finally {
            channelSftp.disconnect();
        }
    }

    public boolean isConnected() {
        System.out.println(Thread.currentThread() + " MySftp::isConnected");
        return session.isConnected();
    }

    protected void disconnect() {
        System.out.println(Thread.currentThread() + " MySftp::disconnect");
        if (session.isConnected()) {
            session.disconnect();
        }
    }

    private ChannelSftp makeChannelSftp() throws JSchException {
        System.out.println(Thread.currentThread() + " MySftp::makeChannelSftp");
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        return channelSftp;
    }
}
