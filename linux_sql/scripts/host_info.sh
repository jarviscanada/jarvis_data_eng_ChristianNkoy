#!/bin/bash

#Store CLI parameters as variables
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#Make sure # of args is correct
# shellcheck disable=SC1009
if [ $# -ne 5 ]; then
  echo 'Incorrect number of arguments. It must be 5.'
  exit 1
fi

#Save machine stats in mb and get host name and memory info
lscpu_out=$(lscpu)
hostname=$(hostname -f)
mem_info=$(cat /proc/meminfo)

#Extract hardware specs
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "^Architecture:" | awk '{print$2}' | xargs)
cpu_model=$(echo "$lscpu_out"  | egrep "^Model name:" | awk '{print$3, $4, $5, $6, $7, $8, $9, $10}' | xargs)
cpu_mhz=$(echo "$lscpu_out"  | egrep "^CPU MHz:" | awk '{print$3}' | xargs)
l2_cache=$(echo "$lscpu_out"  | egrep "^L2 cache:" | awk '{print$3}' | xargs)
total_mem=$(echo "$mem_info" | egrep "^MemTotal:" | awk '{print$2}' | xargs)

timestamp=$(vmstat -t | awk '{print $18, $19}' | tail -n1 | xargs)

#Store long insert statement into a variable
#Insert statement inserts hardware specs data into host_info table
#Id is incremented automatically, no need to insert id value
insert_stmt="INSERT INTO host_info(hostname,cpu_number,cpu_architecture,cpu_model,cpu_mhz,L2_cache,total_mem,timestamp)
            VALUES('$hostname',$cpu_number,'$cpu_architecture','$cpu_model',$cpu_mhz,'$l2_cache',$total_mem,'$timestamp')"

#Environment variable for psql
export PGPASSWORD=$psql_password

#Execute insert statement
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit $?