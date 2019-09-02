psql -U postgres --command "CREATE USER humidity WITH PASSWORD 'humidity';"
createdb -E UTF8 -U postgres -O humidity humidity 
psql -U postgres --command "GRANT ALL PRIVILEGES ON DATABASE humidity TO humidity"
psql -U postgres --command 'CREATE EXTENSION "uuid-ossp"' humidity
psql -U postgres --command 'CREATE EXTENSION "pgcrypto"' humidity