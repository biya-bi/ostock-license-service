#!/bin/bash
export JWT_ISSUER_URI=`cat ${JWT_ISSUER_URI_FILE}`

java org.springframework.boot.loader.launch.JarLauncher