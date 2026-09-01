docker build -t <account>/identity-service:0.0.1 .

Push docker image to Docker Hub
docker image push <account>/identity-service:0.0.1

Create network:
docker network create phidungx-network

Start MySQL in phidungx-network
docker run --network phidungx-network --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:9.6.0-oracle

Run your application in phidungx-network
docker run --name identity-service --network phidungx-network -p 8080:8080 -e DBMS_CONNECTION=jdbc:mysql://mysql:3306/mysql_database identity-service:0.0.1