#!/bin/bash

# Start the first script and wait for it to finish
gnome-terminal -- bash -c "./configs.sh; touch /tmp/configs_done; exec bash"

# Wait until the first script signals completion
while [ ! -f /tmp/confis_done ]; do
    sleep 1
done

# Start the second script
gnome-terminal -- bash -c "./authentication.sh; exec bash"


# Start the third script
gnome-terminal -- bash -c "./category.sh; exec bash"

# Clean up temporary files
rm -f /tmp/configs_done /tmp/authentication_done /tmp/category_done 

