#!/bin/bash
properties_file=$ERIC_ODP_HOME/ldap.properties

source /ericsson/tor/data/global.properties
source /ericsson/enm/key-management/lib/kms_opendj_password.sh
export_opendj_password
echo LDAP_BIND_PW=$OPENDJ_PWD >> $properties_file
echo LDAP_ADMIN_CN=$LDAP_ADMIN_CN >> $properties_file
echo COM_INF_LDAP_ROOT_SUFFIX=$COM_INF_LDAP_ROOT_SUFFIX >> $properties_file
echo COM_INF_LDAP_PORT=$COM_INF_LDAP_PORT >> $properties_file