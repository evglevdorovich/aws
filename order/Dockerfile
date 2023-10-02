FROM maven:3-amazoncorretto-17 AS build
RUN mkdir /app
WORKDIR /app
COPY . /app
RUN mvn clean package

FROM amazoncorretto:17.0.6-alpine3.17
RUN mkdir /app
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
COPY --from=build /app/target/order.jar /app/order.jar
WORKDIR /app
RUN chown -R javauser:javauser /app
USER javauser
CMD "java" "-XX:MaxRAMPercentage=85" "-XX:MinRAMPercentage=50"  "-jar" "aws.jar"
