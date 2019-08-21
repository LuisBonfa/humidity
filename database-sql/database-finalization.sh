#!/usr/bin/env bash
psql -U postgres --command "REVOKE CONNECT ON DATABASE humidity FROM humidity"
psql -U postgres --command "SELECT pid, pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = current_database() AND pid <> pg_backend_pid();"
psql -U postgres --command "DROP DATABASE humidity"
psql -U postgres --command "DROP USER humidity"
