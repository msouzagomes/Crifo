FROM azul/zulu-openjdk-alpine:14
MAINTAINER Alan Santo <alan.santo@terceiros.calcard.com.br>

# Add the service itself
ADD target/crivo-api*.jar /usr/share/api/app.jar

# Startup service
ENTRYPOINT ["java", "-jar", "/usr/share/api/app.jar", "-Djava.net.preferIPv4Stack=true"]