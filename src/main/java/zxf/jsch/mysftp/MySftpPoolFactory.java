package zxf.jsch.mysftp;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PoolUtils;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.nio.file.Path;

public class MySftpPoolFactory {
    public static ObjectPool<MySftp> createPool(String username, Path identity, String host, int port) {
        System.out.println(Thread.currentThread() + " MySftpPoolFactory::createPool");
        SessionFactory sessionFactory = new SessionFactory(username, identity, host, port);
        PooledObjectFactory<MySftp> mySftpPooledObjectFactory = PoolUtils.synchronizedPooledFactory(new MySftpFactory(sessionFactory));
        GenericObjectPoolConfig<MySftp> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(10);
        config.setMinIdle(2);
        config.setMaxWaitMillis(30 * 1000);
        config.setTestOnBorrow(true);
        config.setTestWhileIdle(true);
        return PoolUtils.synchronizedPool(new GenericObjectPool<>(mySftpPooledObjectFactory, config));
    }
}
