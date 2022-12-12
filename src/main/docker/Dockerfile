FROM eclipse-temurin:17-jdk-focal as build

COPY . repository
RUN cd /repository && \
    ./gradlew build && \
    tar xf /repository/build/distributions/shopping-platform.tar -C /opt

FROM eclipse-temurin:17-jre-focal

RUN groupadd -g 9000 shopping-platform && \
    useradd -u 9000 -g shopping-platform shopping-platform-user

USER shopping-platform-user:shopping-platform
COPY --from=build /opt/shopping-platform /opt/shopping-platform

EXPOSE 8080
CMD ["/opt/shopping-platform/bin/shopping-platform"]