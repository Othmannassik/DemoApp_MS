#!/bin/sh

set -e

host="$1"
port="$2"

echo "Waiting for RabbitMQ to be ready..."
echo "Host: $host"
echo "Port: $port"

# Add more debugging information
while ! nc -zv -w 1 "$host" "$port"; do
    echo "RabbitMQ is not yet ready. Retrying..."
    sleep 2
done

echo "RabbitMQ is ready!"
