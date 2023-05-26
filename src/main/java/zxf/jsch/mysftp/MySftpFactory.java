package zxf.jsch.mysftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.pastdev.jsch.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

@Slf4j
public class MySftpFactory extends BasePooledObjectFactory<MySftp> {
    private SessionManager sessionManager;

    public MySftpFactory(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public MySftp create() throws Exception {
        ChannelSftp channelSftp = (ChannelSftp) sessionManager.getSession().openChannel("sftp");
        channelSftp.connect();
        return new MySftp(channelSftp);
    }

    @Override
    public PooledObject<MySftp> wrap(MySftp mySftp) {
        return new DefaultPooledObject<>(mySftp);
    }

    @Override
    public void destroyObject(PooledObject<MySftp> pooledObject) throws Exception {
        MySftp mySftp = pooledObject.getObject();
        mySftp.disconnect();
    }

    @Override
    public boolean validateObject(PooledObject<MySftp> pooledObject) {
        MySftp mySftp = pooledObject.getObject();
        return mySftp.isConnected();
    }

    @Override
    public void passivateObject(PooledObject<MySftp> pooledObject) throws Exception {
        MySftp mySftp = pooledObject.getObject();
        try {
            mySftp.cd(mySftp.getHome());
        } catch (SftpException ex) {
            log.error("Could not reset channel to home folder, closing it", ex);
            mySftp.disconnect();
        }
    }
}
