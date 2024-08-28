#!/bin/bash

HOSTNAME=$(echo $HOSTNAME)
touch $HOME/log_file
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
echo "$(date +"%F %T")  IN Function:  check_user_session_started" >> $HOME/log_file
user_sessions=0
timeout=90
while [ $timeout -gt 0 ]
do
  user_sessions=$(ps aux | grep "sshd: ${HOSTNAME}" | grep -v grep | wc -l)
  sleep 5
  let timeout-=5
  echo "$(date +"%F %T")   Timeout:  ${timeout}    User_sessions:  ${user_sessions}   $(ps aux | grep "sshd: ${HOSTNAME}")" >> $HOME/log_file
done

if [ $user_sessions -eq 0 ]; then
  logger "User ${HOSTNAME} Failed to connect the pod within 90 seconds"
  echo "$(date +"%F %T")  Killing, Failed to connect the pod within 90 seconds" >> $HOME/log_file
  pkill -15 sshd
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
echo "$(date +"%F %T")  IN Function:  check_user_session_running" >> $HOME/log_file
user_sessions=1
#grep_pattern="sshd: ${HOSTNAME}"
while [ $user_sessions -gt 0 ]
do
  user_sessions=$(ps aux | grep "sshd: ${HOSTNAME}" | grep -v grep | wc -l)
  echo "$(date +"%F %T")    User_sessions in While loop:  ${user_sessions}    $(ps aux | grep "sshd: ${HOSTNAME}")" >> $HOME/log_file
  sleep 10
done

logger "User ${HOSTNAME} closed all his sessions"
echo "$(date +"%F %T")  Killing" >> $HOME/log_file
pkill -15 sshd
echo "$(date +"%F %T")  Killed" >> $HOME/log_file
}

#////////////////////////////////////////////
# call check_user function.
#////////////////////////////////////////////
check_user_session_started
check_user_session_running
