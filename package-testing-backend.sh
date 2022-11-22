cd leocode-backend
chmod +x ./mvnw
./mvnw package -DskipTests

cd ../testing-api
chmod +x ./mvnw
./mvnw package -DskipTests
