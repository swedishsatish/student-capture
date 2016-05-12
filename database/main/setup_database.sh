#!/bin/bash
db_superuser=postgres
db_host=nan
db_port=nan
db_username=nan
db_userpswd=nan
db_name=nan

while getopts ":s:u:w:h:p:n:" opt; do
  case $opt in
    s)
      db_superuser=$OPTARG
      ;;
	u)
      db_username=$OPTARG
      ;;
	w)
      db_userpswd=$OPTARG
      ;;
	h)
      db_host=$OPTARG
      ;;
	p)
      db_port=$OPTARG
      ;;
	n)
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

psql -U $db_superuser -W -c "CREATE USER $db_username WITH PASSWORD '$db_userpswd';"
psql -U $db_superuser -W -c "CREATE DATABASE $db_test WITH OWNER $db_username TEMPLATE template0 ENODING 'SQL_ASCII' TABLESPACE pg_default LC_COLLATE 'C' LC_CTYPE 'C' CONNECTION LIMIT -1"