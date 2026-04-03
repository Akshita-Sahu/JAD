#!/bin/bash

# define newest jad's version
JAD_VERSION=${project.version}

# define newest jad's lib home
JAD_LIB_HOME=${HOME}/.jad/lib/${JAD_VERSION}/jad

# exit shell with err_code
# $1 : err_code
# $2 : err_msg
exit_on_err()
{
    [[ ! -z "${2}" ]] && echo "${2}" 1>&2
    exit ${1}
}

# install to local if necessary
if [[ ! -x ${JAD_LIB_HOME} ]]; then

    # install to local
    mkdir -p ${JAD_LIB_HOME} \
    || exit_on_err 1 "create target directory ${JAD_LIB_HOME} failed."

    # copy jar files
    cp *.jar ${JAD_LIB_HOME}/

    # make it -x
    chmod +x ./jad.sh

fi

echo "install to local succeeded."

