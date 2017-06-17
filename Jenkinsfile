#!/usr/bin/env groovy
 
def majorVersion   = "1"
def minorVersion   = "0"
def buildNumber
def buildSuffix    = "Final"
def version        = "$majorVersion.$minorVersion"
def runSystemTests = false
def gradleTasks    = []
 
if (env.BRANCH_NAME == "master") {
    buildNumber = env.BUILD_NUMBER
    gradleTasks = [
        "releaseBom",
        "clean",
        "build",
        "artifactoryPublish",
        "-Pversion=$majorVersion.$minorVersion.$buildNumber.$buildSuffix"
    ]
} else {
    buildNumber = "${env.BUILD_NUMBER}.${convertBranchName(env.BRANCH_NAME)}"
    gradleTasks = [
        "installBillOfMaterials",
        "clean",
        "build"
    ]
}
 
node('docker-registry') {
    stage 'Checkout'
    checkout scm

    timeout(time: 60, unit: 'MINUTES') {
        stage 'Gradle Build / Test'
        sh "chmod +x gradlew"

        try {
            sh "./gradlew ${gradleTasks.join(" ")}"
        } catch (Exception e) {
            error "Failed: ${e}"
            throw (e)
        } finally {
            junit allowEmptyResults: true, keepLongStdio: true, testResults: '**/build/test-results/**/*.xml'
        }
    }
}
 
def convertBranchName(String name) {
    return name.replaceAll('/', '_')
}

