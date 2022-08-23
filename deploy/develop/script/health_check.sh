#!/bin/bash
# (1) 상태를 체그하는 URL입니다. 구현에 맞게 변경
url="http://localhost:8080/board/1"

timeout=5
online=false

echo "Checking status of $url."

# (2) 해당 URL을 curl로 실행해 HttpStatus가 200이 나올 때까지 10번 시도합니다.
loopCount=1
while [ $loopCount -le 10 ]
do
  code=$(curl -sL --connect-timeout 20 --max-time 30 -w "%{http_code}\\n" "$url" -o /dev/null)
  echo "try $loopCount => code: $code"
  if [ "$code" = "200" ];
    then
      online=true
      break
    else
      loopCount=$((loopCount + 1))
      sleep $timeout
  fi
done

# (3) 체크가 성공했다면 다음 작업(다음 서버 배포)을 실행하고, 그렇지 않으면 배포 중단
if $online;
  then
    echo "Monitor finished, website is online."
    exit 0 #Success
  else
    echo "Monitor failed, website seems to be down."
    exit 1 # Failed
fi