package zxf.jsch.mysftp;

import com.jcraft.jsch.JSchException;
import com.pastdev.jsch.DefaultSessionFactory;
import com.pastdev.jsch.SessionFactory;
import com.pastdev.jsch.SessionManager;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PoolUtils;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.nio.file.Path;

public class MySftpPoolFactory {
    public static ObjectPool<MySftp> createPool(String username, Path identity, String host, int port) throws JSchException {
        SessionManager sessionManager = new SessionManager(createSessionFactory(username, identity, host,port));
        PooledObjectFactory<MySftp> mySftpPooledObjectFactory = PoolUtils.synchronizedPooledFactory(new MySftpFactory(sessionManager));
        GenericObjectPoolConfig<MySftp> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(10);
        config.setMinIdle(1);
        config.setMaxWaitMillis(30 * 1000);
        config.setTestOnBorrow(true);
        config.setTestWhileIdle(true);
        return PoolUtils.synchronizedPool(new GenericObjectPool<>(mySftpPooledObjectFactory, config));
    }

    private static SessionFactory createSessionFactory(String username, Path identity, String host, int port) throws JSchException {
        DefaultSessionFactory defaultSessionFactory = new DefaultSessionFactory(username, host, port);
        defaultSessionFactory.setConfig("StrictHostKeyChecking", "no");
        defaultSessionFactory.setConfig("PreferredAuthentications", "publickey");
        defaultSessionFactory.setConfig("ConnectTimeout", "30000");
        defaultSessionFactory.setIdentityFromPrivateKey(identity.toString());
        return defaultSessionFactory;
    }
}
