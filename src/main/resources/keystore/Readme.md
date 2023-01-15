# Generate JKS KeyStore Using KeyTool and Export Certificate from KeyStore
- keytool -genkeypair -alias myPrivateKey -keypass changeit -keyalg RSA -keysize 2048 -validity 3650 -dname "CN=John Smith, OU=Development, O=Standard Supplies Inc., L=Anytown, S=North Carolina, C=US" -keystore mykeystore.jks -storepass changeit -storetype JKS -v
- keytool -export -keystore mykeystore.jks -storetype JKS -storepass changeit -alias myPrivateKey -rfc -file certificate.pem

# Export cert and private key from jks keystore
- keytool -importkeystore -srckeystore mykeystore.jks -destkeystore mykeystore.p12 -deststoretype PKCS12
- openssl pkcs12 -in mykeystore.p12 -nokeys -out server.cer.pem
- openssl pkcs12 -in mykeystore.p12 -nodes -nocerts -out server.key.pem

# Extracting SSH keys from a Java keystore (jks) file
## Convert keystore from jks to pkcs12 forma
- keytool -importkeystore -srckeystore mykeystore.jks -srcalias myPrivateKey -srcstorepass changeit -srckeypass changeit -destkeystore mykeystore.p12 -destalias myPrivateKey -deststorepass changeit -destkeypass changeit -deststoretype PKCS12
## Extract private key & cert from pkcs12 keystore:
- openssl pkcs12 -in mykeystore.p12 -out server.key-cert.pem -passin pass:changeit -passout pass:changeit
## Extract public key in ssh format:
- ssh-keygen -P changeit -y -f server.key-cert.pem > my-sshkey.pub