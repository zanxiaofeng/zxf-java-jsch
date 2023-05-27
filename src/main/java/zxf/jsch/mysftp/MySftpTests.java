package zxf.jsch.mysftp;

import org.apache.commons.pool2.ObjectPool;

import java.nio.file.Paths;

public class MySftpTests {
    public static void main(String[] args) throws Exception {
        ObjectPool<MySftp> mySftpPool = MySftpPoolFactory.createPool("sftp-user", Paths.get("./src/main/resources/keystore/my-sshkey"), "localhost", 2222);

        MySftp mySftp1 = mySftpPool.borrowObject();
        try {
            mySftp1.ls("/upload").forEach(System.out::println);
        } catch (Exception ex) {
            mySftpPool.invalidateObject(mySftp1);
            mySftp1 = null;
        } finally {
            if (mySftp1 != null) {
                mySftpPool.returnObject(mySftp1);
            }
        }

        MySftp mySftp2 = mySftpPool.borrowObject();
        try {
            mySftp2.ls("/").forEach(System.out::println);
        } catch (Exception ex) {
            mySftpPool.invalidateObject(mySftp2);
            mySftp2 = null;
        }

        MySftp mySftp3 = mySftpPool.borrowObject();
        try {
            mySftp3.ls("/upload").forEach(System.out::println);
        } catch (Exception ex) {
            mySftpPool.invalidateObject(mySftp3);
            mySftp3 = null;
        }

        if (mySftp2 != null) {
            mySftpPool.returnObject(mySftp2);
        }

        if (mySftp3 != null) {
            mySftpPool.returnObject(mySftp3);
        }

        mySftpPool.close();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
