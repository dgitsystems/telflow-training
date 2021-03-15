FROM ${docker.registry}/telflow-artefact:${telflow.artefact.docker.tag}

ARG karaf_deploy=/opt/telflow/${component.name}/deploy
ARG bundle_name=${artifactId}-${version}.jar
ARG build_version_label=${docker.preciseVersion}

LABEL com.telflow.solution-version=\${build_version_label}
LABEL org.label-schema.vcs-ref-solution=\${buildNumber}
LABEL org.label-schema.build-date-solution="${build.time}"

COPY --chown=telflow:root $bundle_name $karaf_deploy
