#!/bin/bash

ENV="dev"

while getopts e: option
do
    case "$option" in
        e)
            if [ "$OPTARG" == "dev" -o "$OPTARG" == "uat" -o "$OPTARG" == "prod" -o "$OPTARG" == "bugfix" ]; then
               ENV="$OPTARG"
            fi;;
        \?)
            echo "Usage: args [-e n]"
            echo "-e means environment, dev uat prod bugfix"
            exit 1;;
    esac
done


/opt/tomcat/bin/shutdown.sh
sleep 20
rm -rf /opt/tomcat/webapps/oms-tasly-ext-web
rm /opt/tomcat/webapps/oms-tasly-ext-web.war
#rm -rf /opt/tomcat/webapps/dataonboarding-webapp
#rm /opt/tomcat/webapps/dataonboarding-webapp.war

mkdir  /opt/scripts/downloads/oms -p
unzip /opt/scripts/downloads/oms-extension.war -d /opt/scripts/downloads/oms
cp /opt/scripts/downloads/oms/oms-tasly-ext-web-1.0-SNAPSHOT.war /opt/tomcat/webapps/oms-tasly-ext-web.war
#cp /opt/scripts/downloads/oms/dataonboarding-webapp.war /opt/tomcat/webapps/dataonboarding-webapp.war

cp /opt/scripts/downloads/oms/lib/$ENV/*.* /opt/tomcat/lib/
mv /opt/tomcat/lib/omsserver.sh /opt/tomcat/bin/
chmod 755 /opt/tomcat/bin/omsserver.sh
rm -fr /opt/scripts/downloads/oms

/opt/tomcat/bin/omsserver.sh