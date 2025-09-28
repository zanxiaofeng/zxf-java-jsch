package zxf.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

public class JschFactory {
    static {
        JSch.setLogger(new Logger() {
            @Override
            public boolean isEnabled(int i) {
                return true;
            }

            @Override
            public void log(int i, String s) {
                System.out.println(Thread.currentThread() + " jsch:: " + s);
            }
        });
    }

    public static Session createSession(String username, String passwd, String host, int port) throws Exception {
        System.out.println(Thread.currentThread() + " JschFactory::createSession");
        JSch jSch = new JSch();
        Session session = jSch.getSession(username, host, port);
        session.setPassword(passwd);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PreferredAuthentications", "password");
        session.setServerAliveInterval(120);
        session.setTimeout(3000);
        session.connect();
        return session;
    }

    public static Session createSession(String username, Path identity, String host, int port) throws Exception {
        System.out.println(Thread.currentThread() + " JschFactory::createSession");
        JSch jSch = new JSch();
        jSch.addIdentity(identity.toString());
        Session session = jSch.getSession(username, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PreferredAuthentications", "publickey");
        session.setServerAliveInterval(120);
        session.setTimeout(3000);
        session.connect();
        return session;
    }


    public static Session createSession(String username, String jksFile, String keyPass, String keyAlias, String host, int port) throws Exception {
        System.out.println(Thread.currentThread() + " JschFactory::createSession");
        JSch jSch = new JSch();
        jSch.addIdentity(null, getPrivateKeyFromJKS(jksFile, keyPass, keyAlias), null, null);
        Session session = jSch.getSession(username, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PreferredAuthentications", "publickey");
        session.setServerAliveInterval(120);
        session.setTimeout(3000);
        session.connect();
        return session;
    }

    private static byte[] getPrivateKeyFromJKS(String jksFile, String keyPass, String alias) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream inputStream = Files.newInputStream(Paths.get(jksFile))) {
            keyStore.load(inputStream, keyPass.toCharArray());
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPass.toCharArray());

            StringWriter stringWriter = new StringWriter();
            try (JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(stringWriter)) {
                jcaPEMWriter.writeObject(privateKey);
            }
            return stringWriter.toString().getBytes(StandardCharsets.UTF_8);
        }
    }
}
