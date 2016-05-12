
#!/bin/bash

db_user=postgres_user
db_name=postgres_db
db_host=int-nat.cs.umu.se
db_port=25432

path_to_tables=./create-db.sql

while getopts ":u:f:h:p:d:" opt; do
  case $opt in
	u)
      db_user=$OPTARG
      ;;
	f)
      path_to_tables=$OPTARG
      ;;
	h)
      db_host=$OPTARG
      ;;
	p)
      db_port=$OPTARG
      ;;
	d)
      db_name=$OPTARG
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      exit 1
      ;;
    :)
      echo "Option -$OPTARG requires an argument." >&2
      exit 1
      ;;
  esac
done

psql -h $db_host -p $db_port -d $db_name -U $db_user -W -f $path_to_tables