set -e


sleep 5


mc alias set local http://minio:9000 minioadmin minioadmin


mc mb --ignore-existing local/my-bucket

