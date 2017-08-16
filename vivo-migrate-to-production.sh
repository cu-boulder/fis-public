#!/bin/bash
#------------------------------------------------
# Setup Script for CU-Boulder DB migration
#
#
#------------------------------------------------

umask 002

PRODDB=target-db
SETUPDB=source-db
DBHOST=vivohost.myschool.edu
DBUSER=vivouser
DBPASS=vivopassword
TD=$(date +"%Y%m%d")


PRODDATA=targetvivo/vdata 
SETUPDATA=sourcevivo/vdata

MYSQL=/usr/local/mysql/bin
PROJECT=vivo-cub
HOME=/usr/local/vivo
BACKUP=${HOME}/backup
ANT_HOME=/usr/local/ant
JAVA_HOME=/usr/local/jdk

#Create a backup of the original db
echo "Creating Database Backup of Production"
$MYSQL/mysqldump -u $DBUSER -p$DBPASS --host $DBHOST $PRODDB > $BACKUP/$PROJECT/database-$TD.sql

#Create a backup of the original data
echo "Creating backup of the original data folder"
tar -czf $BACKUP/$PROJECT/data-$TD.tar.gz $HOME/$PRODDATA

#Create a backup of the original build
#echo "Creating backup of the original build"
#tar -czf $BACKUP/$PROJECT/build-$TD.tar.gz $HOME/$PROJECT

#Shutdown Tomcat
#sudo /sbin/service tomcat.tomcat-cub stop
echo "Shutting down tomcat"
./tomcat-cub-control stop

#Copy the deploy file to the root location
#DRE echo "Copying deploy.properties to root location"
#DRE sudo cp $HOME/$PROJECT/fis/deploy-cuboulder/deploy.properties.production $HOME/$PROJECT/deploy.properties
#DRE sudo chown tomcat:tomcat $HOME/$PROJECT/deploy.properties

#Copy the setup data directory to the production data directory
echo "Making a backup and copying image and index data to production location"
#sudo rm -rf $HOME/$PRODDATA/*

#sudo rm -rf $HOME/${PRODDATA}-old
rm -rf $HOME/${PRODDATA}-old
mv $HOME/$PRODDATA $HOME/${PRODDATA}-old
cp -rp $HOME/$SETUPDATA $HOME/$PRODDATA
cp  $HOME/vivo-cub/vivo-cub/runtime.properties.vivo-cub $HOME/$PRODDATA/runtime.properties

# DRE 04/16/2015 commented out sudo so we can try it without sudo using the chmod command
#sudo chown -R tomcat:tomcat $HOME/$PRODDATA
#chmod -R 775 $HOME/$PRODDATA
chmod -R g+rw,o+r $HOME/$PRODDATA

#Copy the setup database to the production database
echo "Exporting Setup Database"
$MYSQL/mysqldump -u $DBUSER -p$DBPASS -h $DBHOST $SETUPDB > /tmp/$PROJECT-database.sql
echo "Importing Setup Database into Production"
$MYSQL/mysql -u $DBUSER -p$DBPASS -h $DBHOST $PRODDB < /tmp/$PROJECT-database.sql

#Due to an issue this morning I'm clearing the webapps folder
#sudo rm -rf /usr/local/tomcat/webapps/*

#Run Ant All
#sudo su - tomcat -c "setenv JAVA_HOME $JAVA_HOME; cd $HOME/$PROJECT; $ANT_HOME/bin/ant all"  #change to ant war

#Startup Tomcat
#sudo /sbin/service tomcat.tomcat-cub start
./tomcat-cub-control start

#umask 022
udate=`date "+%a %b %d %R %Z %Y"`
sed -i "s/Data updated last .*/Data updated last $udate/g" /data/tomcat/tomcat-cub/webapps/vivo/themes/cu-boulder/templates/footer.ftl
#cp /tmp/footer.ftl /data/tomcat/tomcat-cub/webapps/vivo/themes/cu-boulder/templates/footer.ftl
#chmod g+rw,o+r /data/tomcat/tomcat-cub/webapps/vivo/themes/cu-boulder/templates/footer.ftl
#chmod g+rw /tmp/footer.ftl
sleep 240
tail -f /data/tomcat/tomcat-cub/logs/catalina.out | while read line
do
#echo $line
if [[ $line == "INFO: Server startup in"* ]]  && pkill -P $$ tail; then
  echo "Tomcat started - VIVO is up"
  break
fi
done
