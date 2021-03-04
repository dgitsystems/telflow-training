#!/bin/sh

# Create sim link to log directory
mkdir -p /opt/telflow/log/ic-notification-driven
chown telflow:root /opt/telflow/log/ic-notification-driven
ln -sf /opt/telflow/ic-notification-driven/log /opt/telflow/log/ic-notification-driven
chown -h telflow:root /opt/telflow/ic-notification-driven/log
chown telflow:root /opt/telflow /opt/telflow/ic-notification-driven /opt/telflow/log

# Run the application
java -cp '/opt/telflow/ic-notification-driven/lib/*' com.telflow.training.ic.Main
