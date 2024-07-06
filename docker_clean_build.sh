#!/bin/bash

# Function to execute commands in a directory
execute_commands() {
    local dir="$1"
    local image_name="$2"

    echo "Entering directory: $dir"
    cd "$dir" || exit

    echo "Removing Docker image: $image_name"
    docker rmi "$image_name"

    echo "Running Maven clean"
    mvn clean

    echo "Running Maven compile and Jib Docker build"
    mvn compile jib:dockerBuild

    echo "Finished tasks in $dir"
    echo
}

# Main script
main() {
    start_time=$(date +%s)

    execute_commands "accountservice" "5hrey45/accountservice"
    execute_commands "../loanservice" "5hrey45/loanservice"
    execute_commands "../cardservice" "5hrey45/cardservice"
    execute_commands "../configserver" "5hrey45/configserver"
    execute_commands "../eurekaserver" "5hrey45/eurekaserver"
    execute_commands "../gatewayserver" "5hrey45/gatewayserver"

    end_time=$(date +%s)
    duration=$((end_time - start_time))

    echo "All tasks completed. Total time taken: $duration seconds."
}

# Run the main function
main
