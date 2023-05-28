package zxf.jsch.mysftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Vector;

@Data
@AllArgsConstructor
public class MySftp {
    private final Session session;

    public void cd(String folder) throws SftpException, JSchException {
        ChannelSftp channelSftp = this.makeChannelSftp();
        try {
            channelSftp.cd(folder);
        } finally {
            channelSftp.disconnect();
        }
    }

    public Vector ls(String path) throws SftpException, JSchException {
        ChannelSftp channelSftp = this.makeChannelSftp();
        try {
            return channelSftp.ls(path);
        } finally {
            channelSftp.disconnect();
        }
    }

    public void disconnect() {
        System.out.println(Thread.currentThread() + " MySftp::disconnect");
        if (session.isConnected()) {
            session.disconnect();
        }
    }

    public boolean isConnected() {
        return session.isConnected();
    }

    private ChannelSftp makeChannelSftp() throws JSchException {
        System.out.println(Thread.currentThread() + " MySftp::makeChannelSftp");
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        return channelSftp;
    }
}
