FROM ${docker.registry}/telflow-reference-process:${telflow.reference.process.docker.tag}

ARG karaf_deploy=/opt/telflow/process/deploy/
ARG bundle_name=${artifactId}-${version}.jar
ARG build_version_label=${docker.preciseVersion}

LABEL com.telflow.solution-version=\${build_version_label}
LABEL org.label-schema.vcs-ref-solution=\${buildNumber}
LABEL org.label-schema.build-date-solution="${build.time}"

COPY --chown=telflow:root *.jar $karaf_deploy
