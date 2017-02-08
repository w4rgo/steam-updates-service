docker run -it --name "steam-updates-service" \
    --restart=always \
    -v /docker/containers/steam-updates:/updates \
    -p 4567:4567 \
    w4rgo/steam-updates-service