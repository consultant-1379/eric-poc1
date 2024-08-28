ARG ERIC_ENM_SLES_BASE_SCRIPTING_IMAGE_NAME=eric-enm-sles-base
ARG ERIC_ENM_SLES_BASE_SCRIPTING_IMAGE_REPO=armdocker.rnd.ericsson.se/proj-enm
ARG ERIC_ENM_SLES_BASE_SCRIPTING_IMAGE_TAG=1.52.0-23

FROM ${ERIC_ENM_SLES_BASE_SCRIPTING_IMAGE_REPO}/${ERIC_ENM_SLES_BASE_SCRIPTING_IMAGE_NAME}:${ERIC_ENM_SLES_BASE_SCRIPTING_IMAGE_TAG}

ARG BUILD_DATE=unspecified
ARG IMAGE_BUILD_VERSION=unspecified
ARG GIT_COMMIT=unspecified
ARG ISO_VERSION=unspecified
ARG RSTATE=unspecified

LABEL \
com.ericsson.product-number="CXC 174 2010" \
com.ericsson.product-revision=$RSTATE \
enm_iso_version=$ISO_VERSION \
org.label-schema.name="ENM OnDemand Pods Manager Service Group" \
org.label-schema.build-date=$BUILD_DATE \
org.label-schema.vcs-ref=$GIT_COMMIT \
org.label-schema.vendor="Ericsson" \
org.label-schema.version=$IMAGE_BUILD_VERSION \
org.label-schema.schema-version="1.0.0-rc1"

RUN zypper ref && zypper update -y


RUN zypper install -y EXTRjdk11_CXP9038676 && ln -s /usr/java/jdk11* /usr/java/openjdk_latest


ENV JAVA_HOME=/usr/java/openjdk_latest
ENV PATH=$JAVA_HOME/bin:$PATH

