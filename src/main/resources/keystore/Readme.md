# Generate jks keystore using keytool and export certificate from keystore
- keytool -genkeypair -alias myPrivateKey -keypass changeit -keyalg RSA -keysize 2048 -validity 3650 -dname "CN=John Smith, OU=Development, O=Standard Supplies Inc., L=Anytown, S=North Carolina, C=US" -keystore mykeystore.jks -storepass changeit -storetype JKS -v
- keytool -export -keystore mykeystore.jks -storetype JKS -storepass changeit -alias myPrivateKey -rfc -file certificate.pem

# Export cert and private key from jks keystore
- keytool -importkeystore -srckeystore mykeystore.jks -srcalias myPrivateKey -srcstorepass changeit -srckeypass changeit -destkeystore mykeystore.p12 -destalias myPrivateKey -deststorepass changeit -destkeypass changeit -deststoretype PKCS12
- openssl pkcs12 -in mykeystore.p12 -nokeys -out server.cert.pem -passin pass:changeit
- openssl pkcs12 -in mykeystore.p12 -nodes -nocerts -out server.key.pem -passin pass:changeit

# Extracting SSH keys from a Java keystore (jks) file
## Convert keystore from jks to pkcs12 format
- keytool -importkeystore -srckeystore mykeystore.jks -srcalias myPrivateKey -srcstorepass changeit -srckeypass changeit -destkeystore mykeystore.p12 -destalias myPrivateKey -deststorepass changeit -destkeypass changeit -deststoretype PKCS12
## Method 1
### Extract private key(Encrypted) & cert from pkcs12 keystore:
- openssl pkcs12 -in mykeystore.p12 -out server.key-cert.pem -passin pass:changeit -passout pass:changeit
### Extract public key in ssh format:
- ssh-keygen -P changeit -y -f ./server.key-cert.pem > my-sshkey.pub
### Transfer OpenSSH Private key to RSA Private Key in place
- ssh-keygen -p -P changeit -N "" -m pem -f ./server.key-cert.pem
- mv ./server.key-cert.pem ./my-sshkey
## Method 2
### Extract private key from pkcs12 keystore:
- openssl pkcs12 -in mykeystore.p12 -passin pass:changeit -nodes -nocerts -out server.key.pem
### Transfer OpenSSH Private key to RSA Private Key in place
- ssh-keygen -p -P changeit -N "" -m pem -f ./server.key.pem
- mv ./server.key.pem ./my-sshkey
### Extract public key in ssh format:
- ssh-keygen -y -f ./my-sshkey > my-sshkey.pub

# Test sftp by username/password
## sftp -v -P <port> <username>@<remote_host>
- sftp -v -P 2222 sftp-user@localhost
- ls /upload

# Test sftp by username/identity
## curl -v -k -u <username>: --key ~/.ssh/id_rsa sftp://<remote_host>/<remote_path>
- curl -v -k -u sftp-user: --key ./my-sshkey sftp://localhost:2222/upload/
## sftp -v -i ~/.ssh/id_rsa -P <port> <username>@<remote_host>
- sftp -v -i ./my-sshkey -P 2222 sftp-user@localhost
- ls /upload