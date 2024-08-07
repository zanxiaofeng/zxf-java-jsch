# 四种认证模式
- password
- publickey(RSA,DSA)
- keyboard-interactive
- gss-api-with-mic

# Core Classes
- com.jcraft.jsch.JSch
- com.jcraft.jsch.ChannelSftp;
- com.jcraft.jsch.ChannelExec
- com.jcraft.jsch.ChannelShell
- com.jcraft.jsch.JSchException;
- com.jcraft.jsch.Session;
- com.jcraft.jsch.SftpException;
- com.jcraft.jsch.Logger;
- com.jcraft.jsch.HostKey
- com.jcraft.jsch.HostKeyRepository
- com.jcraft.jsch.KnownHosts
- com.jcraft.jsch.ConfigRepository
- com.jcraft.jsch.OpenSSHConfig

# SSH Keep Alive
## Server side(/etc/ssh/sshd_config)
- Server每隔60秒给客户端发送一次保活信息包给客户端
- ClientAliveInterval 60
- Server端发出的请求客户端没有回应的次数达到86400次的时候就断开连接，正常情况下客户端都会响应
- ClientAliveCountMax 86400
## Client side(/etc/ssh/ssh_config)
- Client 每隔60秒给客户端发送一次保活信息包给客户端
- ServerAliveInterval 60
- Client 端发出的请求服务端没有回应的次数达到86400次的时候就断开连接，正常情况下服务端都会响应
- ServerAliveCountMax 86400

# SSH Client-Server trust
## Server authentication
- /etc/ssh/known_hosts
- ~/.ssh/known_hosts
## User authentication
- username/password
- ~/.ssh/authorized_keys

# How Jsch process un-know hosts
- Session::setConfig("StrictHostKeyChecking", "no");
- com.jcraft.jsch.HostKey
- com.jcraft.jsch.HostKeyRepository
- com.jcraft.jsch.KnownHosts
