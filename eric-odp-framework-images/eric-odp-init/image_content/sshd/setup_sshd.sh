ERIC_ODP_SSHD_CONFIG_DIR="$ERIC_ODP_HOME/sshd"

mkdir -p $ERIC_ODP_SSHD_CONFIG_DIR

ssh-keygen -f $ERIC_ODP_SSHD_CONFIG_DIR/ssh_host_rsa_key -N ''

ls -al $ERIC_ODP_SSHD_CONFIG_DIR

cp /usr/local/etc/sshd_config $ERIC_ODP_SSHD_CONFIG_DIR
