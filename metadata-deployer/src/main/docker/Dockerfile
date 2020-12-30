FROM ${docker.registry}/telflow-metadata-deploy:${telflow.metadata.deploy.docker.tag}

LABEL com.telflow.solution-version="${docker.preciseVersion}"
LABEL org.label-schema.vcs-ref-solution=\${buildNumber}
LABEL org.label-schema.build-date-solution="${build.time}"

COPY --chown=telflow:root metadata /opt/telflow/metadata-deployer/deploy