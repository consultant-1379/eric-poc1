ARG ERIC_ODP_BASE_IMAGE_NAME=eric-odp-sles-base
ARG ERIC_ODP_BASE_IMAGE_REPO=armdocker.rnd.ericsson.se/proj_oss_releases/enm
ARG ERIC_ODP_BASE_IMAGE_TAG=latest

FROM ${ERIC_ODP_BASE_IMAGE_REPO}/${ERIC_ODP_BASE_IMAGE_NAME}:${ERIC_ODP_BASE_IMAGE_TAG}

ARG BUILD_DATE=unspecified
ARG GIT_COMMIT=unspecified
ARG ISO_VERSION=unspecified
ARG RSTATE=unspecified

LABEL \
com.ericsson.product-number="CXC 174 2010" \
com.ericsson.product-revision=$RSTATE \
enm_iso_version=$ISO_VERSION \
org.label-schema.name="ERICSSON ODP INIT IMAGE" \
org.label-schema.build-date=$BUILD_DATE \
org.label-schema.vcs-ref=$GIT_COMMIT \
org.label-schema.vendor="Ericsson" \
org.label-schema.version=$IMAGE_BUILD_VERSION \
org.label-schema.schema-version="1.0.0-rc1"


RUN zypper install -y ERICkeymanagement_CXP9031887 && zypper clean --all && \
    chmod 655 /ericsson/enm/key-management/lib

COPY image_content/scripts/*.sh /usr/local/bin/

RUN chmod 777 /usr/local/bin/*.sh