cd ~/NetBeansProjects/cinemaCal
mvn clean package
scp -r -p ~/NetBeansProjects/cinemaCal/target/cinemaCal-1.0.jar root@YOUR_DOMAIN:/home/ubuntu/cinemagenda/
scp -r -p ~/NetBeansProjects/cinemaCal/start-cinemagenda.sh root@YOUR_DOMAIN:/home/ubuntu/cinemagenda/
