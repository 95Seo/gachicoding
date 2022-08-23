# (1) 빌드된 파일 디렉터리 설정입니다. 폴더내 최근 1개 파일을 선택합니다.
REPOSITORY=/home
BUILD_PATH=$(ls -tr ${REPOSITORY}/build/lib/*.jar | tail -1)
JAR_NAME=$(basename $BUILD_PATH)
echo "> build file: $JAR_NAME"

# (2) 업로드된 파일을 작업 폴더로 이동합니다. 이동 후 빌드 파일 링크를 신규 파일로 대체합니다.
echo "> copy build file"
DEPLOY_PATH=${REPOSITORY}/deploy/
if [ ! -d $DEPLOY_PATH ]; then
  mkdir $DEPLOY_PATH
fi

cp $BUILD_PATH $DEPLOY_PATH

echo "> change file name"
CP_JAR_PATH=$DEPLOY_PATH$JAR_NAME
APPLICATION_JAR_NAME=application-deploy.jar
APPLICATION_JAR=$DEPLOY_PATH$APPLICATION_JAR_NAME

echo "> create link"
ln -Tfs $CP_JAR_PATH $APPLICATION_JAR

# (3) 기존 서버가 살아 있으면 다운시킵니다.
echo "> Check application PID."
CURRENT_PID=$(pgrep -f -n $APPLICATION_JAR_NAME)
echo "$CURRENT_PID"

if [ -z $CURRENT_PID ];
  then
    echo "> No running application found."
  else
    echo "> kill -9 $CURRENT_PID"
    kill -9 $CURRENT_PID
    sleep 10
fi

# (4) 애플리케이션을 실행합니다. '-Dspring.profiles.active-local'은 운영환경에 맞게 조절
echo "> Run application."
nohub java -jar -Dspring.profiles.active=local $APPLICATION_JAR > /dev/null 2> /dev/null < /dev/null &