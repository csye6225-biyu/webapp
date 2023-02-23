#!/bin/bash

# Update package manager
sudo yum update -y

# Install Java
sudo yum install java-17-amazon-corretto-devel -y

# Install PostgreSQL
sudo yum install -y postgresql-server

# Configure PostgreSQL
sudo postgresql-setup initdb
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Change the "ident" authentication method to "md5" of PostgreSQL
sudo cp /var/lib/pgsql/data/pg_hba.conf /var/lib/pgsql/data/pg_hba.conf.bak
sudo sed -i 's/ident/md5/g' /var/lib/pgsql/data/pg_hba.conf
sudo systemctl restart postgresql

#Create a new PostgreSQL database and user
sudo su - postgres -c "psql -c \"CREATE DATABASE mydatabase;\""
sudo su - postgres -c "psql -c \"CREATE USER biyu WITH PASSWORD 'password';\""
sudo su - postgres -c "psql -c \"GRANT ALL PRIVILEGES ON DATABASE mydatabase TO biyu;\""

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
ExecStart=/usr/bin/java -jar ${JAR_PATH}
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl start csye6225webapp
sudo systemctl enable csye6225webapp