#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

usage() {
    cat <<EOF
Usage: $(basename "$0") [options] [-- <extra mvn args>]

Options:
  --fast              ： clean +  site  + Maven (-T 1C)
  --no-clean           Maven clean（ target ）
  --skip-site          site （vuepress/yarn），
  -T, --threads <arg>  Maven （ 1C/4）
  -h, --help          
EOF
}

NO_CLEAN=false
SKIP_SITE=false
MVN_THREADS=""
MVN_EXTRA_ARGS=()

while [[ $# -gt 0 ]]; do
    case "$1" in
        --fast)
            NO_CLEAN=true
            SKIP_SITE=true
            [[ -z "${MVN_THREADS}" ]] && MVN_THREADS="1C"
            shift
            ;;
        --no-clean)
            NO_CLEAN=true
            shift
            ;;
        --skip-site)
            SKIP_SITE=true
            shift
            ;;
        -T|--threads)
            if [[ $# -lt 2 ]]; then
                echo "Missing value for $1" 1>&2
                usage 1>&2
                exit 2
            fi
            MVN_THREADS="$2"
            shift 2
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        --)
            shift
            MVN_EXTRA_ARGS+=("$@")
            break
            ;;
        *)
            MVN_EXTRA_ARGS+=("$1")
            shift
            ;;
    esac
done

get_local_maven_project_version()
{
    "$DIR/mvnw" org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate \
     -Dexpression=project.version -q -DforceStdout -f "$DIR/pom.xml"
}

"$DIR/mvnw" -version

CUR_VERSION=$(get_local_maven_project_version)

# jad's version
DATE=$(date '+%Y%m%d%H%M%S')

JAD_VERSION="${CUR_VERSION}.${DATE}"

echo "${JAD_VERSION}" > $DIR/core/src/main/resources/com/akshita-sahu/jad/core/res/version

# define newset jad lib home
NEWEST_JAD_LIB_HOME=${HOME}/.jad/lib/${JAD_VERSION}/jad


# exit shell with err_code
# $1 : err_code
# $2 : err_msg
exit_on_err()
{
    [[ ! -z "${2}" ]] && echo "${2}" 1>&2
    exit ${1}
}

# maven package the jad
MVN_ARGS=(-f "$DIR/pom.xml" -Dmaven.test.skip=true -DskipTests=true -Dmaven.javadoc.skip=true)
if [[ -n "${MVN_THREADS}" ]]; then
    MVN_ARGS+=(-T "${MVN_THREADS}")
fi
if [[ "${SKIP_SITE}" == "true" ]]; then
    MVN_ARGS+=(-Djad.site.frontend.skip=true)
fi
if [[ "${NO_CLEAN}" == "true" ]]; then
    MVN_GOALS=(package)
else
    MVN_GOALS=(clean package)
fi

# maven package the jad
"$DIR/mvnw" "${MVN_ARGS[@]}" "${MVN_EXTRA_ARGS[@]}" "${MVN_GOALS[@]}" \
|| exit_on_err 1 "package jad failed."

rm -r "$DIR/core/src/main/resources/com/akshita-sahu/jad/core/res/version"

packaging_bin_path=$(ls "${DIR}"/packaging/target/jad-bin.zip)

# install to local
mkdir -p "${NEWEST_JAD_LIB_HOME}"
unzip ${packaging_bin_path} -d "${NEWEST_JAD_LIB_HOME}/"

# print ~/.jad directory size
jad_dir_size="$(du -hs ${HOME}/.jad | cut -f1)"
echo "${HOME}/.jad size: ${jad_dir_size}"
