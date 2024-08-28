ARG ERIC_ODP_BASE_IMAGE_NAME=eric-odp-sles-base
ARG ERIC_ODP_BASE_IMAGE_REPO=armdocker.rnd.ericsson.se/proj_oss_releases/enm
ARG ERIC_ODP_BASE_IMAGE_TAG=latest
ARG IMAGE_BUILD_VERSION

#FROM ${ERIC_ODP_BASE_IMAGE_REPO}/${ERIC_ODP_BASE_IMAGE_NAME}:${IMAGE_BUILD_VERSION}

FROM ${ERIC_ODP_BASE_IMAGE_REPO}/${ERIC_ODP_BASE_IMAGE_NAME}:${ERIC_ODP_BASE_IMAGE_TAG}

ARG BUILD_DATE=unspecified
ARG GIT_COMMIT=unspecified
ARG ISO_VERSION=unspecified
ARG RSTATE=unspecified

LABEL \
com.ericsson.product-number="CXC 174 2010" \
com.ericsson.product-revision=$RSTATE \
enm_iso_version=$ISO_VERSION \
org.label-schema.name="ERICSSON SSH/SFTP PROXY MAIN CONTAINER IMAGE" \
org.label-schema.build-date=$BUILD_DATE \
org.label-schema.vcs-ref=$GIT_COMMIT \
org.label-schema.vendor="Ericsson" \
org.label-schema.version=$IMAGE_BUILD_VERSION \
org.label-schema.schema-version="1.0.0-rc1"

RUN zypper install -y libcap-progs libcups2 && zypper clean --all
RUN mkdir -p /ericsson/pod_setup

COPY image_content/startup_script.sh /ericsson/pod_setup

RUN chmod 755 /ericsson/pod_setup/startup_script.sh

CMD ["/ericsson/pod_setup/startup_script.sh"]