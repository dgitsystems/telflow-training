#!/bin/sh

# Create sim link to log directory
mkdir -p /opt/telflow/log/ic-process-driven
chown telflow:root /opt/telflow/log/ic-process-driven
ln -sf /opt/telflow/ic-process-driven/log /opt/telflow/log/ic-process-driven
chown -h telflow:root /opt/telflow/ic-process-driven/log
chown telflow:root /opt/telflow /opt/telflow/ic-process-driven /opt/telflow/log

# Run the application
java -cp '/opt/telflow/ic-process-driven/lib/*' com.telflow.training.ic.Main
