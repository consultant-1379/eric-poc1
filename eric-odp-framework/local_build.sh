export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
export MAVEN_HOME=/workspace/maven/apache-maven-3.9.5
#export MAVEN_OPTS="-Djsse.enableSNIExtension=false -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Djsse.enableSNIExtension=false"

$MAVEN_HOME/bin/mvn clean package install deploy \
-Dquarkus.container-image.build=true \
-Dquarkus.container-image.push=true \
-Dquarkus.container-image.registry=armdocker.rnd.ericsson.se \
-Dquarkus.container-image.group=proj_oss_releases/enm \
-Dquarkus.container-image.name=eric-odp-k8s-operator \
-Dquarkus.operator-sdk.bundle.package-name=eric-odp-k8s-operator \
-Dquarkus.operator-sdk.bundle.channels=alpha
