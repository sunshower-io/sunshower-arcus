pipeline {
    agent {
        kubernetes {
            yamlFile 'deployments/agent/template.yaml'
        }
    }
    environment {

        /**
         * credentials for Artifactory
         */
        MVN_REPO = credentials('artifacts-credentials')


        /**
         * github credentials
         */
        GITHUB = credentials('github-build-credentials')

        /**
         * this just contains the build email address and name
         */
        GITHUB_USER = credentials("github-build-userinfo")

        /**
         * current version
         */
        CURRENT_VERSION = readMavenPom(file: 'sunshower-env/pom.xml').getVersion()

        /**
         * base URL components
         */
        REPOSITORY_BASE = "http://artifacts.sunshower.cloud/repository/"


        /**
         * release repository path component
         */
        MVN_RELEASES = "sunshower-releases"


        /**
         * snapshot repository path component
         */
        MVN_SNAPSHOTS = "sunshower-snapshots"
    }

    stages {

        stage('Checkout') {
            steps {
                scmSkip(deleteBuild: true, skipPattern: '\\[released\\].*')
            }

        }

        stage('build env poms') {

            steps {
                container('maven') {
                    sh """
                        mvn clean install deploy -f bom
                    """

                }
            }
        }

        /**
         * we could probably eventually handle this via plugin, but the process is
         *
         * Upon merge to master:
         *
         * 1. Increment the version number
         * 2. Update the version-number in the POM files
         * 3. Rebuild the Maven/Gradle projects
         * 4. Upon success, increment to next snapshot
         * 5. Upon failure, fail
         * 6. Push next snapshot to master
         */
        stage('deploy master snapshot') {
            when {
                branch 'master'
            }
            steps {
                scmSkip(deleteBuild: true, skipPattern: '\\[released\\].*')


                container('maven') {
                    script {
                        segs = (env.CURRENT_VERSION - '-SNAPSHOT').split('\\.')
                        env.NEXT_VERSION = "${segs.join('.')}-${env.BUILD_NUMBER}-SNAPSHOT"
                    }

                }
            }
        }


        stage("release component") {
            when {
                expression {
                    env.GIT_BRANCH == "release/${env.CURRENT_VERSION}"
                }
            }

            steps {
                scmSkip(deleteBuild: true, skipPattern: '\\[released\\].*')

                container('maven') {

                    script {
                        /**
                         * strip the leading "release/" prefix
                         */
                        env.TAG_NAME = env.GIT_BRANCH - "release/"

                        /**
                         * compute the next versions:
                         *
                         * RELEASED_VERSION is the version that we're
                         * 1. building
                         * 2. deploying
                         * 3. tagging
                         *
                         * NEXT_VERSION is the version that main will be incremented to
                         *
                         * So, if CURRENT_VERSION = 1.0.0-SNAPSHOT,
                         * then RELEASED_VERSION = 1.0.0.Final
                         * and NEXT_VERSION = 1.0.1-SNAPSHOT
                         */
                        version = env.CURRENT_VERSION


                        segs = (version - '-SNAPSHOT')
                                .split('\\.')
                                .collect { i ->
                                    i as int
                                }

                        releasedVersion = (segs[0..-2] << ++segs[-1]).join('.')
                        nextVersion = releasedVersion + "-SNAPSHOT"

                        env.NEXT_VERSION = nextVersion
                        env.RELEASED_VERSION = "${releasedVersion}.Final"
                    }


                    /**
                     * configure github email address
                     */
                    sh """
                        git config --global user.email "${GITHUB_USER_USR}"
                    """

                    /**
                     * configure
                     */
                    sh """
                        git config --global user.name "${GITHUB_USER_PSW}"
                    """

                    sh """
                        mkdir -p ~/.ssh
                    """

                    sh """
                        ssh-keyscan -t rsa github.com >> ~/.ssh/known_hosts
                    """

                    sh """
                        git remote set-url origin https://${GITHUB_PSW}@github.com/sunshower-io/sunshower-devops
                    """
                }
            }
        }
    }
}