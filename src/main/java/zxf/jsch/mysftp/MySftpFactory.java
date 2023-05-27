package zxf.jsch.mysftp;

import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

@Slf4j
public class MySftpFactory extends BasePooledObjectFactory<MySftp> {
    private SessionFactory sessionFactory;

    public MySftpFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public MySftp create() throws Exception {
        System.out.println("MySftpFactory::create");
        Session session = sessionFactory.createSession();
        System.out.println("MySftpFactory::create.connect");
        session.connect();
        return new MySftp(session);
    }

    @Override
    public PooledObject<MySftp> wrap(MySftp mySftp) {
        System.out.println("MySftpFactory::wrap");
        return new DefaultPooledObject<>(mySftp);
    }

    @Override
    public void destroyObject(PooledObject<MySftp> pooledObject) throws Exception {
        System.out.println("MySftpFactory::destroyObject");
        MySftp mySftp = pooledObject.getObject();
        mySftp.disconnect();
    }

    @Override
    public boolean validateObject(PooledObject<MySftp> pooledObject) {
        System.out.println("MySftpFactory::validateObject");
        MySftp mySftp = pooledObject.getObject();
        return mySftp.isConnected();
    }

    @Override
    public void passivateObject(PooledObject<MySftp> pooledObject) throws Exception {
        System.out.println("MySftpFactory::passivateObject");
        MySftp mySftp = pooledObject.getObject();
        try {
            mySftp.cd(mySftp.getHome());
        } catch (SftpException ex) {
            log.error("Could not reset channel to home folder, closing it", ex);
            mySftp.disconnect();
        }
    }
}
