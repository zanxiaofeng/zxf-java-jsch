package zxf.jsch.mysftp;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Logger;
import org.apache.commons.pool2.ObjectPool;

import java.nio.file.Paths;

public class MySftpTests {
    public static void main(String[] args) throws Exception {
        ObjectPool<MySftp> mySftpPool = MySftpPoolFactory.createPool("sftp-user", Paths.get("./src/main/resources/keystore/my-sshkey"), "localhost", 2222);
        JSch.setLogger(new Logger() {
            @Override
            public boolean isEnabled(int i) {
                return true;
            }

            @Override
            public void log(int i, String s) {
                System.out.println("jsch:: " + s);
            }
        });

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
        } finally {
            if (mySftp2 != null) {
                mySftpPool.returnObject(mySftp2);
            }
        }

        mySftpPool.clear();
    }
}
