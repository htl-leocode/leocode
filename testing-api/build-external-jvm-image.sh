rm -rf target
mvn -Dquarkus.package.type=uber-jar -DskipTests verify
docker build -t ghcr.io/musikfreunde/testing-api:32.0 .
docker push ghcr.io/musikfreunde/testing-api:32.0
