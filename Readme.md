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

# Core setting of SSH Client & Server
## SSH Client
- /etc/ssh/ssh_config
- /home/<user>/.ssh/known_hosts
- /home/<user>/.ssh/id_rsa & id_rsa.pub
- /home/<user>/.ssh/id_dsa & id_dsa.pub
- /home/<user>/.ssh/id_ecdsa & id_ecdsa.pub
## SSH Server
- /etc/ssh/sshd_config
- /etc/ssh/ssh_host_key & ssh_host_key.pub
- /etc/ssh/ssh_host_rsa_key & ssh_host_rsa_key.pub
- /etc/ssh/ssh_host_dsa_key & ssh_host_dsa_key.pub
- /etc/ssh/ssh_host_ecdsa_key & ssh_host_ecdsa_key.pub
- /home/<user>/.ssh/authorized_keys

# sftp command
- `sftp -vvvv -o StrictHostKeyChecking=no -o IdentityFile=/path/to/id_rsa sftpuser@sftp.host`

# How Jsch process un-know hosts
- Session::setConfig("StrictHostKeyChecking", "no");
- com.jcraft.jsch.HostKey
- com.jcraft.jsch.HostKeyRepository
- com.jcraft.jsch.KnownHosts

# int chown(const char *pathname, uid_t owner, gid_t group)
- These system calls change the owner and group of a file.
- Only a privileged process (Linux: one with the CAP_CHOWN capability) may change the owner of a file.  The owner of a file may change the group of the file to any group of which that owner is a member.  A privileged process (Linux: with CAP_CHOWN) may change the group arbitrarily.
- If the owner or group is specified as -1, then that ID is not changed.

# Ownership of new files
- When a new file is created (by, for example, open(2) or mkdir(2)), its owner is made the same as the filesystem user ID of the creating process.  The group of the file depends on a range of factors, including the type of filesystem, the options used to mount the filesystem, and whether or not the set-group-ID mode bit is enabled on the parent directory.  If the filesystem supports the -o grpid (or, synonymously -o bsdgroups) and -o nogrpid (or, synonymously -o sysvgroups) mount(8) options, then the rules are as follows:
- If the filesystem is mounted with -o grpid, then the group of a new file is made the same as that of the parent directory.
- If the filesystem is mounted with -o nogrpid and the set- group-ID bit is disabled on the parent directory, then the group of a new file is made the same as the process's filesystem GID.
- If the filesystem is mounted with -o nogrpid and the set- group-ID bit is enabled on the parent directory, then the group of a new file is made the same as that of the parent directory.
- As at Linux 4.12, the -o grpid and -o nogrpid mount options are supported by ext2, ext3, ext4, and XFS.  Filesystems that don't support these mount options follow the -o nogrpid rules.

# How to generate public key from private key
- openssl rsa -in <key>.pem -pubout -outform PEM|DER|PVK -out <pubkey>.pem
- ssh-keygen -y -f <key>.pem
