#!/bin/bash

### bamboo_docker_build.sh
### To be used as part of the bamboo build
### Usage, from a script task inside bamboo:  bamboo_docker_build.sh ${bamboo.inject.branchVersion} ${bamboo.inject.release} ${bamboo.buildNumber} ${bamboo.build.working.directory}


# Change these to match your build
DockerRegistry="registry.cloud.telflow.com/dgit_private"
TelflowComponent="telflow-training-assets"
DockerfilePath="docker/Dockerfile"

#You probably don't want to change the variables below this line
LongTelflowVersion=$1
Release=$2
BuildNumber=$3
BuildDirectory=$4

TelflowVersion=${LongTelflowVersion%??}
TelflowVersionLabel="1.0.0"

TelflowVersionDevLabel="$LongTelflowVersion.dev"
TelflowVersionDevCurrent="$TelflowVersion.dev.current"
TelflowVersionCurrent="$TelflowVersion.current"

cd "$BuildDirectory"

#build
function buildDocker() {
  docker build  --label Version="$TelflowVersionLabel" -t "$DockerRegistry/$TelflowComponent":"$TelflowVersionLabel"  -f "$DockerfilePath" . || exit

#tag and push
  if [ "$Release" == "develop" ] || [ "$Release" == "pre" ]; then
    docker tag "$DockerRegistry/$TelflowComponent":"$TelflowVersionLabel" "$DockerRegistry/$TelflowComponent:latest" || exit
    docker tag "$DockerRegistry/$TelflowComponent":"$TelflowVersionLabel" "$DockerRegistry/$TelflowComponent:$TelflowVersionDevLabel" || exit
    docker tag "$DockerRegistry/$TelflowComponent":"$TelflowVersionLabel" "$DockerRegistry/$TelflowComponent:$TelflowVersionDevCurrent" || exit

    docker push "$DockerRegistry/$TelflowComponent:latest" || exit
    docker push "$DockerRegistry/$TelflowComponent":"$TelflowVersionDevLabel" || exit
    docker push "$DockerRegistry/$TelflowComponent":"$TelflowVersionDevCurrent" || exit

  elif [ "$Release" == "release" ]; then
    docker tag "$DockerRegistry/$TelflowComponent":"$TelflowVersionLabel" "$DockerRegistry/$TelflowComponent:latest" || exit
    docker tag "$DockerRegistry/$TelflowComponent":"$TelflowVersionLabel" "$DockerRegistry/$TelflowComponent:$LongTelflowVersion" || exit
    docker tag "$DockerRegistry/$TelflowComponent":"$TelflowVersionLabel" "$DockerRegistry/$TelflowComponent:$TelflowVersionCurrent" || exit

    docker push "$DockerRegistry/$TelflowComponent:latest" || exit
    docker push "$DockerRegistry/$TelflowComponent":"$LongTelflowVersion" || exit
    docker push "$DockerRegistry/$TelflowComponent":"$TelflowVersionCurrent" || exit

  fi
}

buildDocker

#housekeeping
docker system prune -f
docker image prune -a -f --filter="label!=Component=TelflowBaseImage"
