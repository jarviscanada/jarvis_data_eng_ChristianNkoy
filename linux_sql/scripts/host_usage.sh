#!/bin/bash
#Store CLI parameters as variables
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#Check # of args
if [ $# -ne  5 ]; then
  echo 'Incorrect number of arguments. It must be 5.'
  exit 1
fi

#Machine stats and host name
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

#Usage data
memory_free=$(echo "$vmstat_mb" | awk '{print $4}'| tail -n1 | xargs)
cpu_idle=$(echo "$vmstat_mb" | awk '{print $15}'| tail -n1 | xargs)
cpu_kernel=$(echo "$vmstat_mb" | awk '{print $14}'| tail -n1 | xargs)
disk_io=$(echo "$vmstat_mb" | awk '{print $16}'| tail -n1 | xargs)

disk_free=$(df -BM)
disk_available=$(echo "$disk_free" | awk '{print $4}'| sed -n 6p | xargs)

timestamp=$(vmstat -t | awk '{print $18, $19}' | tail -n1 | xargs)

#Get host_id from host_info table
host_id="(SELECT host_id FROM host_info WHERE hostname='$hostname')";

#Insert data into host_usage table
insert_stmt="INSERT INTO host_usage(timestamp,host_id,memory_free,cpu_idle,cpu_kernel,disk_io,disk_available)
            VALUES('$timestamp',$host_id,$memory_free,$cpu_idle,$cpu_kernel,$disk_io,'$disk_available')"

#Environment variable
export PGPASSWORD=$psql_password

#Execute insert statement
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?
