FROM jenkins/jenkins:2.332.2-lts

ENV JENKINS_USER admin
ENV JENKINS_PASS admin

# Skip initial setup
# Disable script approval checks
# Allow local git checkout
ENV JAVA_OPTS -Xmx2048m -Djava.awt.headless=true -Djenkins.install.runSetupWizard=false -Dpermissive-script-security.enabled=true -Dhudson.plugins.git.GitSCM.ALLOW_LOCAL_CHECKOUT=true


COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/plugins.txt