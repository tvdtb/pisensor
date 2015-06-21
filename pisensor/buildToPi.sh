mvn -P raspberry,wildfly-swarm clean package
scp target/pisensor-swarm.jar pi@192.168.178.62:pisensor-swarm.jar