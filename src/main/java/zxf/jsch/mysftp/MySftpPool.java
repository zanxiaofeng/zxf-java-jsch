package zxf.jsch.mysftp;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PoolUtils;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class MySftpPool {
    private final ObjectPool<MySftp> connectionPool;

    public MySftpPool(MySftpProperties mySftpProperties) {
        MySftpFactory mySftpFactory = new MySftpFactory(mySftpProperties);
        PooledObjectFactory<MySftp> pooledObjectFactory = PoolUtils.synchronizedPooledFactory(mySftpFactory);
        this.connectionPool = PoolUtils.synchronizedPool(new GenericObjectPool<>(pooledObjectFactory, mySftpProperties.getPoolConfig()));
    }

    public MySftp getMySftp() throws Exception {
        System.out.println(Thread.currentThread() + " MySftpPool::getMySftp");
        try {
            return connectionPool.borrowObject();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            throw ex;
        }
    }

    public void releaseMySftp(MySftp mySftp) throws Exception {
        System.out.println(Thread.currentThread() + " MySftpPool::releaseMySftp");
        try {
            if (mySftp != null) {
                connectionPool.returnObject(mySftp);
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            throw ex;
        }
    }


    public void shutdown() {
        System.out.println(Thread.currentThread() + " MySftpPool::shutdown");
        this.connectionPool.close();
    }
}
