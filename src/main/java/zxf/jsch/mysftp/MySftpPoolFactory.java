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
    public static SessionManager createSessionManager(String username, Path identity, String host, int port) throws JSchException {
        DefaultSessionFactory sessionFactory = new DefaultSessionFactory(username, host, port);
        sessionFactory.setConfig("StrictHostKeyChecking", "no");
        sessionFactory.setConfig("PreferredAuthentications", "publickey");
        sessionFactory.setConfig("ConnectTimeout", "30000");
        sessionFactory.setIdentityFromPrivateKey(identity.toString());
        return new SessionManager(sessionFactory);
    }

    public static ObjectPool<MySftp> createPool(SessionManager sessionManager) throws JSchException {
        PooledObjectFactory<MySftp> mySftpPooledObjectFactory = PoolUtils.synchronizedPooledFactory(new MySftpFactory(sessionManager));
        GenericObjectPoolConfig<MySftp> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(10);
        config.setMinIdle(1);
        config.setMaxWaitMillis(30 * 1000);
        config.setTestOnBorrow(true);
        config.setTestWhileIdle(true);
        return PoolUtils.synchronizedPool(new GenericObjectPool<>(mySftpPooledObjectFactory, config));
    }
}
