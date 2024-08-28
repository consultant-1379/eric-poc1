#!/bin/bash

HOSTNAME=$(echo $HOSTNAME)
touch /home/shared/${HOSTNAME}/log_file
#############################################################
#
# Logger Functions
#
#############################################################
info() {
    logger -t "${SCRIPT_NAME}" -p user.notice "INFO ( ${SCRIPT_NAME} ): $1"
}

#######################################
# Action :
#  check if user session exists.
# Globals :
#   HOSTNAME
# Arguments:
#   None
# Returns:
#   None
#######################################
check_user_session_started() {
echo "$(date +"%F %T")  IN Function:  check_user_session_started" >> /home/shared/${HOSTNAME}/log_file
user_sessions=0
timeout=90
while [ $timeout -gt 0 ]
do
  user_sessions=$(ps aux | grep "sshd: ${HOSTNAME}" | grep -v grep | wc -l)
  sleep 5
  let timeout-=5
  echo "$(date +"%F %T")   Timeout:  ${timeout}    User_sessions:  ${user_sessions}   $(ps aux | grep "sshd: ${HOSTNAME}")" >> /home/shared/${HOSTNAME}/log_file
done

if [ $user_sessions -eq 0 ]; then
  logger "User ${HOSTNAME} Failed to connect the pod within 90 seconds"
  echo "$(date +"%F %T")  Killing, Failed to connect the pod within 90 seconds" >> /home/shared/${HOSTNAME}/log_file
  kill -15 $(ps aux | grep '/usr/sbin/sshd -D -f /var/lib/extrausers/sshd/sshd_config -e' | awk '{print $2}' | cut -d$'\n' -f1)
  else
    logger "User ${HOSTNAME} successfully opened the session toward the pod"
fi

}

#######################################
# Action :
#  check if user session still exists.
# Globals :
#   HOSTNAME
# Arguments:
#   None
# Returns:
#   None
#######################################
check_user_session_running() {
echo "$(date +"%F %T")  IN Function:  check_user_session_running" >> /home/shared/${HOSTNAME}/log_file
user_sessions=1
#grep_pattern="sshd: ${HOSTNAME}"
while [ $user_sessions -gt 0 ]
do
  user_sessions=$(ps aux | grep "sshd: ${HOSTNAME}" | grep -v grep | wc -l)
  echo "$(date +"%F %T")    User_sessions in While loop:  ${user_sessions}    $(ps aux | grep "sshd: ${HOSTNAME}")" >> /home/shared/${HOSTNAME}/log_file
  sleep 10
done

logger "User ${HOSTNAME} closed all his sessions"
echo "$(date +"%F %T")  Killing" >> /home/shared/${HOSTNAME}/log_file
kill -15 $(ps aux | grep '/usr/sbin/sshd -D -f /var/lib/extrausers/sshd/sshd_config -e' | awk '{print $2}' | cut -d$'\n' -f1)
echo "$(date +"%F %T")  Killed" >> /home/shared/${HOSTNAME}/log_file
}

#////////////////////////////////////////////
# call check_user function.
#////////////////////////////////////////////
check_user_session_started
check_user_session_running