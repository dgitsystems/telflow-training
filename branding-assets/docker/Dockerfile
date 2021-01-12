FROM registry.cloud.telflow.com/dgit_private/telflow-assets:11.0.dev.current

ENV SOURCE_DIR ./
ENV TMP_INSTALL_DIR /tmp/

COPY ${SOURCE_DIR} ${TMP_INSTALL_DIR}

RUN cp -r .${TMP_INSTALL_DIR}resources/* /var/www/assets/telflow/ \
    && rm -rf ${TMP_INSTALL_DIR}*