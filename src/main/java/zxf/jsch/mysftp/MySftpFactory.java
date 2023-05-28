package zxf.jsch.mysftp;

import com.jcraft.jsch.Session;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class MySftpFactory extends BasePooledObjectFactory<MySftp> {
    private SessionFactory sessionFactory;

    public MySftpFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public MySftp create() throws Exception {
        System.out.println(Thread.currentThread() + " MySftpFactory::create");
        Session session = sessionFactory.createSession();
        System.out.println(Thread.currentThread() + " MySftpFactory::create.connect");
        session.connect();
        return new MySftp(session);
    }

    @Override
    public boolean validateObject(PooledObject<MySftp> pooledObject) {
        System.out.println(Thread.currentThread() + " MySftpFactory::validateObject");
        MySftp mySftp = pooledObject.getObject();
        return mySftp.isConnected();
    }

    @Override
    public PooledObject<MySftp> wrap(MySftp mySftp) {
        System.out.println(Thread.currentThread() + " MySftpFactory::wrap");
        return new DefaultPooledObject<>(mySftp);
    }

    @Override
    public void destroyObject(PooledObject<MySftp> pooledObject) throws Exception {
        System.out.println(Thread.currentThread() + " MySftpFactory::destroyObject");
        MySftp mySftp = pooledObject.getObject();
        mySftp.disconnect();
    }
}
