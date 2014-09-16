echo \*\*\*\* `date` \*\*\*\*
IFS="|";
cat orcid-fac_data.dat |
#cat orcid-test-group.dat |
        while read first last email homedeptalias fisid
        do
                sed "s#ReplaceMe0#$email#;s#ReplaceMe1#$first#;s#ReplaceMe2#$last#g" basic2.txt | /usr/lib/sendmail -odi -f sometrusteduser@yourschool.edu -t
                echo "Email sent to $email for $first $last to $email homedept: $homedeptalias"
                echo "$fisid|$first|$last|$email|$homedeptalias" >> email-results1.out
        done
IFS=$O;
echo \*\*\*\* `date` \*\*\*\*

