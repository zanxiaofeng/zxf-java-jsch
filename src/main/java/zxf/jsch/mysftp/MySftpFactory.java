package zxf.jsch.mysftp;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;
import java.nio.file.Path;

public class MySftpFactory extends BasePooledObjectFactory<MySftp> {
    private final MySftpProperties mySftpProperties;

    public MySftpFactory(MySftpProperties mySftpProperties) {
        this.mySftpProperties = mySftpProperties;
    }

    @Override
    public MySftp create() throws Exception {
        System.out.println(Thread.currentThread() + " MySftpFactory::create");
        return new MySftp(createSession(), mySftpProperties.getBasePath());
    }

    @Override
    public PooledObject<MySftp> wrap(MySftp mySftp) {
        System.out.println(Thread.currentThread() + " MySftpFactory::wrap");
        return new DefaultPooledObject<>(mySftp);
    }

    @Override
    public boolean validateObject(PooledObject<MySftp> pooledObject) {
        System.out.println(Thread.currentThread() + " MySftpFactory::validateObject");
        MySftp mySftp = pooledObject.getObject();
        return mySftp.isConnected();
    }

    @Override
    public void destroyObject(PooledObject<MySftp> pooledObject) throws Exception {
        System.out.println(Thread.currentThread() + " MySftpFactory::destroyObject");
        MySftp mySftp = pooledObject.getObject();
        mySftp.disconnect();
    }

    private Session createSession() throws JSchException, IOException {
        System.out.println(Thread.currentThread() + " MySftpFactory::createSession");
        JSch jSch = new JSch();
        jSch.addIdentity(mySftpProperties.getIdentityFile());
        Session session = jSch.getSession(mySftpProperties.getUsername(), mySftpProperties.getHost(), mySftpProperties.getPort());
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PreferredAuthentications", "publickey");
        session.setServerAliveInterval(mySftpProperties.getServerAliveInterval());
        session.setTimeout(mySftpProperties.getTimeout());
        session.connect();
        return session;
    }

    static {
        JSch.setLogger(new Logger() {
            @Override
            public boolean isEnabled(int i) {
                return true;
            }

            @Override
            public void log(int i, String s) {
                System.out.println(Thread.currentThread() + " jsch:: " + s);
            }
        });
    }
}
