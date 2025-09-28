package zxf.jsch;

import zxf.jsch.mysftp.MySftp;
import zxf.jsch.mysftp.MySftpPool;
import zxf.jsch.mysftp.MySftpProperties;

public class MySftpPoolTests {
    public static void main(String[] args) throws Exception {
        System.out.println(Thread.currentThread() + "####~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        MySftpPool mySftpPool = new MySftpPool(createMySftpProperties());

        MySftp mySftp1 = mySftpPool.getMySftp();
        System.out.println(Thread.currentThread() + "1111~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        MySftp mySftp2 = mySftpPool.getMySftp();
        System.out.println(Thread.currentThread() + "2222~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        MySftp mySftp3 = mySftpPool.getMySftp();
        System.out.println(Thread.currentThread() + "3333~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        MySftp mySftp4 = mySftpPool.getMySftp();
        System.out.println(Thread.currentThread() + "4444~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        mySftpPool.shutdown();
        System.out.println(Thread.currentThread() + "$$$$~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    private static MySftpProperties createMySftpProperties() {
        MySftpProperties mySftpProperties = new MySftpProperties();
        mySftpProperties.setHost("localhost");
        mySftpProperties.setPort(2222);
        mySftpProperties.setUsername("sftp-user");
        mySftpProperties.setIdentityFile("./src/main/resources/keystore/my-sshkey");
        mySftpProperties.setBasePath("/");
        mySftpProperties.setTimeout(3000);
        mySftpProperties.setServerAliveInterval(120);
        mySftpProperties.getPool().setMaxTotal(3);
        mySftpProperties.getPool().setMaxIdle(2);
        mySftpProperties.getPool().setMinIdle(1);
        mySftpProperties.getPool().setTestOnBorrow(true);
        mySftpProperties.getPool().setTestWhileIdle(true);
        mySftpProperties.getPool().setMaxWaitMillis(30000);
        return mySftpProperties;
    }
}
