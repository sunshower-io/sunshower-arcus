pipeline {
    agent {
        kubernetes {
            yamlFile 'agent/template.yaml'
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
        CURRENT_VERSION = readMavenPom(file: 'bom/pom.xml').getVersion()

        /**
         * base URL components
         */
        REPOSITORY_BASE = "http://artifacts.sunshower.cloud/repository"


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


        stage('build env poms') {

            steps {
                container('maven') {
                    script {
                        env.TARGET_REPOSITORY = "${env.REPOSITORY_BASE}/${env.MVN_SNAPSHOTS}"
                    }

                    /**
                     * deploy boms
                     */
                    sh """
                        mvn clean install deploy -f bom
                    """


                    sh """
                        gradle \
                        clean \
                        build \
                        spotlessApply \
                        publishToMavenLocal \
                        publish \
                        -PmavenRepositoryUrl=${env.TARGET_REPOSITORY} \
                        -PmavenRepositoryUsername=${env.MVN_REPO_USR} \
                        -PmavenRepositoryPassword=${env.MVN_REPO_PSW}
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

                container('maven') {
                    script {
                        segs = (env.CURRENT_VERSION - '-SNAPSHOT').split('\\.')
                        env.NEXT_VERSION = "${segs.join('.')}-${env.BUILD_NUMBER}-SNAPSHOT"
                    }

                    sh """
                        mvn versions:set \
                        -f bom \
                        -DnewVersion="${env.NEXT_VERSION}"
                    """

                    sh """
                        mvn clean install deploy -f bom
                    """

                    sh """
                        gradle \
                        clean \
                        build \
                        spotlessApply \
                        publishToMavenLocal \
                        publish \
                        -Pversion=${env.NEXT_VERSION} \
                        -PmavenRepositoryUrl=${env.TARGET_REPOSITORY} \
                        -PmavenRepositoryUsername=${env.MVN_REPO_USR} \
                        -PmavenRepositoryPassword=${env.MVN_REPO_PSW}
                    """
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

                container('maven') {

                    script {
                        env.RELEASE_REPOSITORY = "${env.REPOSITORY_BASE}/${env.MVN_RELEASES}"

                        env.SNAPSHOT_REPOSITORY = "${env.REPOSITORY_BASE}/${env.MVN_SNAPSHOTS}"
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
                        git remote set-url origin https://${GITHUB_PSW}@github.com/sunshower-io/sunshower-arcus
                    """


                    /**
                     * release BOM POM
                     */
                    sh """
                        mvn versions:set \
                        -f bom \
                        -DnewVersion=${env.RELEASED_VERSION} 
                    """

                    sh """
                        mvn clean install deploy -f bom
                    """

                    /**
                     * increment gradle build to next released version and deploy
                     */
                    sh """
                        find . -name gradle.properties | xargs \
                        sed -i 's/version=${env.CURRENT_VERSION}/version=${env.RELEASED_VERSION}/g'
                    """


                    sh """
                        gradle \
                        clean \
                        build \
                        spotlessApply \
                        publishToMavenLocal \
                        publish \
                        -PmavenRepositoryUrl=${env.RELEASE_REPOSITORY} \
                        -PmavenRepositoryUsername=${env.MVN_REPO_USR} \
                        -PmavenRepositoryPassword=${env.MVN_REPO_PSW}
                    """


                    sh """
                        git tag "v${env.RELEASED_VERSION}" \
                        -m "[released] Tagging: ${env.RELEASED_VERSION} (from ${env.TAG_NAME})"
                    """

                    sh """
                        git push origin "v${env.RELEASED_VERSION}"
                    """


                    /**
                     * increment to next snapshot
                     */

                    sh """
                        mvn versions:set \
                        -f bom \
                        -DnewVersion=${env.NEXT_VERSION} 
                    """

                    sh """
                        mvn clean install deploy -f bom
                    """

                    sh """
                        find . -name gradle.properties | xargs \
                        sed -i 's/version=${env.RELEASED_VERSION}/version=${env.NEXT_VERSION}/g'
                    """

                    sh """
                        gradle \
                        clean \
                        build \
                        spotlessApply \
                        publishToMavenLocal \
                        publish \
                        -PmavenRepositoryUrl=${env.SNAPSHOT_REPOSITORY} \
                        -PmavenRepositoryUsername=${env.MVN_REPO_USR} \
                        -PmavenRepositoryPassword=${env.MVN_REPO_PSW}
                    """


                    sh """
                        git commit -am "[released] ${env.TAG_NAME} -> ${env.RELEASED_VERSION}"
                    """

                    sh """
                        git checkout -b master
                    """

                    sh """
                        git pull --rebase origin master
                    """

                    sh """
                        git push -u origin master
                    """
                }
            }
        }
    }

    post {
        always {
            junit "**/build/reports/**/*.xml"
        }
    }
}