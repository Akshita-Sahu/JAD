FROM eclipse-temurin:8-jdk-alpine

ARG JAD_VERSION="4.1.8"
ARG MIRROR=false

ENV MAVEN_HOST=https://repo1.maven.org/maven2 \
    ALPINE_HOST=dl-cdn.alpinelinux.org \
    MIRROR_MAVEN_HOST=https://maven.akshita-sahu.com/repository/public \
    MIRROR_ALPINE_HOST=mirrors.akshita-sahu.com 

# if use mirror change to akshita-sahu mirror site
RUN if $MIRROR; then MAVEN_HOST=${MIRROR_MAVEN_HOST} ;ALPINE_HOST=${MIRROR_ALPINE_HOST} ; sed -i "s/dl-cdn.alpinelinux.org/${ALPINE_HOST}/g" /etc/apk/repositories ; fi && \
    # https://github.com/docker-library/openjdk/issues/76
    apk add --no-cache tini && \ 
    # download & install jad
    wget -qO /tmp/jad.zip "${MAVEN_HOST}/com/akshita-sahu/jad/jad-packaging/${JAD_VERSION}/jad-packaging-${JAD_VERSION}-bin.zip" && \
    mkdir -p /opt/jad && \
    unzip /tmp/jad.zip -d /opt/jad && \
    rm /tmp/jad.zip

# Tini is now available at /sbin/tini
ENTRYPOINT ["/sbin/tini", "--"]
