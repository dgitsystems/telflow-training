#!/bin/sh

# Create sim link to log directory
mkdir -p /opt/telflow/log/ic-outbound-integration
chown telflow:root /opt/telflow/log/ic-outbound-integration
ln -sf /opt/telflow/ic-process-driven/log /opt/telflow/log/ic-outbound-integration
chown -h telflow:root /opt/telflow/ic-outbound-integration/log
chown telflow:root /opt/telflow /opt/telflow/ic-outbound-integration /opt/telflow/log

# Run the application
java -cp '/opt/telflow/ic-outbound-integration/lib/*' com.telflow.training.ic.Main
