package zxf.jsch.mysftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Vector;

@Data
@AllArgsConstructor
public class MySftp {
    private final ChannelSftp channelSftp;

    public void cd(String folder) throws SftpException {
        channelSftp.cd(folder);
    }

    public Vector ls(String path) throws SftpException {
        return channelSftp.ls(path);
    }


    public void disconnect() {
        if (channelSftp.isConnected()) {
            channelSftp.disconnect();
            System.out.println("MySftp::disconnect");
        }
    }

    public boolean isConnected() {
        return channelSftp.isConnected() && !channelSftp.isClosed();
    }

    public String getHome() throws SftpException {
        return channelSftp.getHome();
    }
}
