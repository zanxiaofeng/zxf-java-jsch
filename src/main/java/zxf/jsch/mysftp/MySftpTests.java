package zxf.jsch.mysftp;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Logger;
import com.pastdev.jsch.SessionManager;
import org.apache.commons.pool2.ObjectPool;

import java.nio.file.Paths;

public class MySftpTests {
    public static void main(String[] args) throws Exception {
        SessionManager sessionManager = MySftpPoolFactory.createSessionManager("sftp-user", Paths.get("./src/main/resources/keystore/my-sshkey"), "localhost", 2222);
        ObjectPool<MySftp> mySftpPool = MySftpPoolFactory.createPool(sessionManager);
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
        }

        MySftp mySftp3 = mySftpPool.borrowObject();
        try {
            mySftp3.ls("/upload").forEach(System.out::println);
        } catch (Exception ex) {
            mySftpPool.invalidateObject(mySftp3);
        }

        mySftpPool.close();
        sessionManager.close();
    }
}
