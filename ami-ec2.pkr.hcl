packer {
  required_plugins {
    amazon = {
      version = ">= 1.1.1"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

variable "profile" {
  type    = string
  default = "packer"
}
variable "aws_region" {
  type    = string
  default = "us-west-2"
}

variable "source_ami" {
  type    = string
  default = "ami-0f1a5f5ada0e7da53"
}

variable "ssh_username" {
  type    = string
  default = "ec2-user"
}

variable "demo_account" {
  type    = string
  default = "302916093227"
}


source "amazon-ebs" "my-ami" {
  region          = "${var.aws_region}"
  profile         = "${var.profile}"
  ami_name        = "csye6225_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  ami_description = "AMI fro CSYE 6225 us-west-2"
  ami_users = [
    "${var.demo_account}",
  ]

  ami_regions = [
    "${var.aws_region}"
  ]

  aws_polling {
    delay_seconds = 120
    max_attempts  = 50
  }

  instance_type = "t2.micro"
  source_ami    = "${var.source_ami}"
  ssh_username  = "${var.ssh_username}"

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/xvda"
    volume_size           = 50
    volume_type           = "gp2"
  }
}

build {

  sources = [
    "source.amazon-ebs.my-ami"
  ]

  provisioner "file" {
    source      = "./cloudwatch-config.json"
    destination = "/tmp/"
  }

  provisioner "file" {
    source      = "target/webapp-0.0.1-SNAPSHOT.jar"
    destination = "/home/ec2-user/"
  }

  provisioner "shell" {
    script = "./setup-ec2.sh"
  }
}