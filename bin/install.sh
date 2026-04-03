#! /bin/bash

# temp file of jad.sh
TEMP_JAD_FILE="./jad.sh.$$"

# target file of jad.sh
TARGET_JAD_FILE="./jad.sh"

# update timeout(sec)
SO_TIMEOUT=60

# default downloading url
JAD_FILE_URL="https://github.com/Akshita-Sahu/JAD/jad.sh"

# exit shell with err_code
# $1 : err_code
# $2 : err_msg
exit_on_err()
{
    [[ ! -z "${2}" ]] && echo "${2}" 1>&2
    exit ${1}
}

# check permission to download && install
[[ ! -w ./ ]] && exit_on_err 1 "permission denied, target directory ./ was not writable."

if [[ $# -gt 1 ]] && [[ $1 = "--url" ]]; then
  shift
  JAD_FILE_URL=$1
  shift
fi

# download from akshita-sahuos
echo "downloading... ${TEMP_JAD_FILE}"
curl \
    -sLk \
    --connect-timeout ${SO_TIMEOUT} \
    ${JAD_FILE_URL} \
    -o ${TEMP_JAD_FILE} \
|| exit_on_err 1 "download failed!"

# write or overwrite local file
rm -rf jad.sh
mv ${TEMP_JAD_FILE} ${TARGET_JAD_FILE}
chmod +x ${TARGET_JAD_FILE}

# done
echo "JAD install succeeded."
