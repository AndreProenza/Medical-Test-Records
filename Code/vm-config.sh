#router

# Activate forwarding
change the forwarding variables in /etc/ufw/sysctl.conf
net/ipv4/ip_forward=1
net/ipv6/conf/default/forwarding=1
net/ipv6/conf/all/forwarding=1

sudo ufw route allow in on enp0s3 out to enp0s8
sudo ufw route allow in on enp0s3 out to enp0s9
sudo ufw route allow in on enp0s8 out to enp0s3
sudo ufw route allow in on enp0s8 out to enp0s9
sudo ufw route allow in on enp0s9 out to enp0s3
sudo ufw route allow in on enp0s9 out to enp0s8
sudo ufw enable # activate the firewall


#hospital frontend

sudo ufw enable
sudo ufw allow 8443

# hospital backend
sudo ufw enable
sudo ufw allow from 10.0.0.4 to any proto tcp port 3000

# hospital backend server
sudo ufw enable
sudo ufw allow from 10.0.0.4 to any proto tcp port 4000

# laboratory frontend

sudo ufw enable
sudo ufw allow 8443

# laboratory backend
sudo ufw enable
sudo ufw allow from 10.0.0.4 to any proto tcp port 3000
# sudo ufw status to check the status 

# for both lab and hospital


#--- crontab

crontab -e # create

@reboot /path/to/bash-script

no scritpt:

nohup java -jar /path/to/jar &
