package zxf.jsch.mysftp;

import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

@Data
public class MySftpProperties {
    private String host;
    private Integer port;
    private String username;
    private String identityFile;
    private String basePath;
    private Integer serverAliveInterval;
    private Integer timeout;
    private Pool pool = new Pool();

    public GenericObjectPoolConfig<MySftp> getPoolConfig() {
        GenericObjectPoolConfig<MySftp> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(pool.getMaxTotal());
        poolConfig.setMaxIdle(pool.getMinIdle());
        poolConfig.setMinIdle(pool.getMinIdle());
        poolConfig.setMaxWaitMillis(pool.getMaxWaitMillis());
        poolConfig.setTestOnBorrow(pool.getTestOnBorrow());
        poolConfig.setTestWhileIdle(pool.getTestWhileIdle());
        return poolConfig;
    }

    @Data
    public static class Pool {
        private Integer maxTotal;
        private Integer maxIdle;
        private Integer minIdle;
        private Integer maxWaitMillis;
        private Boolean testOnBorrow;
        private Boolean testWhileIdle;
    }
}
