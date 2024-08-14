FROM centos:latest

LABEL maintainer="nick <alskadmlcraz1@gmail.com>"
LABEL description="nick's backup Server"

RUN sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-*
RUN sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-*

RUN yum update -y && yum install -y net-tools vim openssh-server
RUN echo 'root:1234' | chpasswd

EXPOSE 22

CMD [ "/sbin/init" ]