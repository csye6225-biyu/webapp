#!/bin/bash

sleep 60

# Update package manager
sudo yum update -y

# Install Java
sudo yum install java-17-amazon-corretto-devel -y

# Install cloud watch agent
sudo yum install amazon-cloudwatch-agent -y

# Move cloudwatch-config.json to /opt
sudo mv /tmp/cloudwatch-config.json /opt

# Install Spring Boot application as a systemd service
SERVICE_NAME=csye6225webapp
JAR_PATH=/home/ec2-user/webapp-0.0.1-SNAPSHOT.jar
SERVICE_USER=ec2-user

sudo tee /etc/systemd/system/${SERVICE_NAME}.service > /dev/null << EOF
[Unit]
Description=My Spring Boot App
After=syslog.target

[Service]
User=${SERVICE_USER}
EnvironmentFile=/etc/environment
Restart=always
RestartSec=3
ExecStart=/bin/bash -c 'source /etc/environment && /usr/bin/java -jar ${JAR_PATH}'
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl start csye6225webapp
sudo systemctl enable csye6225webapp