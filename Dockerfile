FROM maven:3-amazoncorretto-17 AS build
RUN mkdir /app
WORKDIR /
COPY ./order /app
COPY pom.xml .
RUN mvn clean package

FROM amazoncorretto:17.0.6-alpine3.17
RUN mkdir /app
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
COPY --from=build app/target/order.jar /app/order.jar
WORKDIR /app
RUN chown -R javauser:javauser /app
USER javauser
EXPOSE 8080
CMD "java" "-XX:MaxRAMPercentage=85" "-XX:MinRAMPercentage=50"  "-jar" "order.jar"

#FROM maven:3-amazoncorretto-17 AS build
#RUN mkdir /app
#WORKDIR /
#COPY ./payment /app
#COPY pom.xml .
#RUN mvn clean package
#
#FROM amazoncorretto:17.0.6-alpine3.17
#RUN mkdir /app
#RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
#COPY --from=build app/target/payment.jar /app/payment.jar
#WORKDIR /app
#RUN chown -R javauser:javauser /app
#USER javauser
#EXPOSE 8081
#CMD "java" "-XX:MaxRAMPercentage=85" "-XX:MinRAMPercentage=50"  "-jar" "payment.jar"

#FROM maven:3-amazoncorretto-17 AS build
#RUN mkdir /app
#WORKDIR /
#COPY ./warehouse /app
#COPY pom.xml .
#RUN mvn clean package
#
#FROM amazoncorretto:17.0.6-alpine3.17
#RUN mkdir /app
#RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
#COPY --from=build app/target/warehouse.jar /app/warehouse.jar
#WORKDIR /app
#RUN chown -R javauser:javauser /app
#USER javauser
#EXPOSE 8082
#CMD "java" "-XX:MaxRAMPercentage=85" "-XX:MinRAMPercentage=50"  "-jar" "warehouse.jar"
