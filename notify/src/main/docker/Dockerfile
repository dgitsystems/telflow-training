FROM ${docker.registry}/telflow-notify:${telflow.notify.docker.tag}

ARG template_notify_dir=/opt/telflow/notify/etc/notify/templates/training
ARG docker_template_dir=templates

ARG karaf_deploy=/opt/telflow/notify/deploy
ARG bundle_name=${artifactId}-${version}.jar
ARG build_version_label=${docker.preciseVersion}

LABEL com.telflow.solution-version=\${build_version_label}
LABEL org.label-schema.vcs-ref-solution=\${buildNumber}
LABEL org.label-schema.build-date-solution="${build.time}"

RUN mkdir -p $template_notify_dir

COPY --chown=telflow:root $bundle_name $karaf_deploy
COPY --chown=telflow:root $docker_template_dir/* $template_notify_dir/