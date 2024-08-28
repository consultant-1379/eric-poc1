ARG ERIC_ODP_MAIN_CONTAINER_IMAGE_NAME=eric-odp-main-container
ARG ERIC_ODP_MAIN_CONTAINER_IMAGE_REPO=armdocker.rnd.ericsson.se/proj_oss_releases/enm
ARG ERIC_ODP_MAIN_CONTAINER_IMAGE_TAG=1.0.0-19
ARG IMAGE_BUILD_VERSION

FROM ${ERIC_ODP_MAIN_CONTAINER_IMAGE_REPO}/${ERIC_ODP_MAIN_CONTAINER_IMAGE_NAME}:${IMAGE_BUILD_VERSION}

ARG BUILD_DATE=unspecified
ARG IMAGE_BUILD_VERSION=unspecified
ARG GIT_COMMIT=unspecified
ARG ISO_VERSION=unspecified
ARG RSTATE=unspecified

LABEL \
com.ericsson.product-number="CXC 174 3052" \
com.ericsson.product-revision=$RSTATE \
enm_iso_version=$ISO_VERSION \
org.label-schema.name="cENM scripting base image on SLES Pipeline" \
org.label-schema.build-date=$BUILD_DATE \
org.label-schema.vcs-ref=$GIT_COMMIT \
org.label-schema.vendor="Ericsson" \
org.label-schema.version=$IMAGE_BUILD_VERSION \
org.label-schema.schema-version="1.0.0-rc1"

#### TORF-596742
## update configuration of zypper to install man pages
RUN sed -i 's/rpm.install.excludedocs = yes/rpm.install.excludedocs = no/g' /etc/zypp/zypp.conf


ENV container eric-odp
RUN echo "container=eric-odp" >> /etc/environment

COPY image_content/repos/*.repo /etc/zypp/repos.d/

## extract content of pam_openam rpm
RUN zypper download ERICpamopenam_CXP9039073 && \
rpm -ivh /var/cache/zypp/packages/enm_iso_repo/ERICpamopenam_CXP9039073*.rpm --allfiles --nodeps --noscripts

## add base packages, that are common across all scripting applications
RUN zypper install -y binutils iputils iproute2 file file-magic && \
    zypper install -y python311 python311-base python311-curses perl perl-base \
    glibc-locale glibc-locale-base netcat-openbsd python3-pyOpenSSL python3-libxml2 python3-pytz screen time vim && \
    zypper clean --all && \
    mkdir -p /usr/share/info && ln -s /var/tmp /usr/tmp
    
#COPY image_content/scripts/check_user_session.sh /usr/local/bin/
COPY image_content/scripts/add_sbin_path_for_enm_users.sh /etc/profile.d/
COPY image_content/scripts/monitor_user_sessions.sh /usr/local/bin/

ENV GLOBAL_CONFIG="/ericsson/tor/data/global.properties"

## fix for missing /var/log/secure in some of the containers
RUN touch /var/log/secure

## apply compatiblity for scripts, that tried to run with default system python
RUN cp /usr/bin/python3 /usr/bin/python