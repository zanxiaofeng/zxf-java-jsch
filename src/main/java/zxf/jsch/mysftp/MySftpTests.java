package zxf.jsch.mysftp;

import org.apache.commons.pool2.ObjectPool;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class MySftpTests {
    public static void main(String[] args) throws Exception {
        MySftpPool mySftpPool = new MySftpPool(createMySftpProperties());

        MySftp mySftp1 = mySftpPool.getMySftp();
        try {
            System.out.println(Thread.currentThread() + " 111~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            mySftp1.list("/upload").forEach(System.out::println);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mySftpPool.releaseMySftp(mySftp1);
        }

        MySftp mySftp2 = mySftpPool.getMySftp();
        try {
            System.out.println(Thread.currentThread() + " 222~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            mySftp2.list(".").forEach(System.out::println);
            System.out.println(Thread.currentThread() + " ############################");
            mySftp2.list("/upload").forEach(System.out::println);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mySftpPool.releaseMySftp(mySftp2);
        }

        MySftp mySftp3 = mySftpPool.getMySftp();
        try {
            System.out.println(Thread.currentThread() + " 333~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            mySftp3.list(".").forEach(System.out::println);
            System.out.println(Thread.currentThread() + " ############################");
            mySftp3.list("/upload").forEach(System.out::println);
            mySftp3.upload("/upload/hello.txt", "Hello".getBytes(StandardCharsets.UTF_8), 0666);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mySftpPool.releaseMySftp(mySftp3);
        }

        mySftpPool.shutdown();
        System.out.println(Thread.currentThread() + " ---~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    private static MySftpProperties createMySftpProperties() {
        MySftpProperties mySftpProperties = new MySftpProperties();
        mySftpProperties.setHost("localhost");
        mySftpProperties.setPort(2222);
        mySftpProperties.setUsername("sftp-user");
        mySftpProperties.setIdentityFile(Paths.get("./src/main/resources/keystore/my-sshkey").toString());
        mySftpProperties.setBasePath("/");
        mySftpProperties.setTimeout(3000);
        mySftpProperties.setServerAliveInterval(120);
        mySftpProperties.getPool().setMaxTotal(20);
        mySftpProperties.getPool().setMaxIdle(2);
        mySftpProperties.getPool().setMinIdle(1);
        mySftpProperties.getPool().setTestOnBorrow(true);
        mySftpProperties.getPool().setTestWhileIdle(true);
        mySftpProperties.getPool().setMaxWaitMillis(30000);
        return mySftpProperties;
    }
}
