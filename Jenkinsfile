#!groovy
def PackageVersion = ''
def BranchToBuild = ''

stage('Config') {
  properties([
    [
      $class: 'ParametersDefinitionProperty',
      parameterDefinitions: [
        [
          $class: 'StringParameterDefinition',
          defaultValue: 'develop',
          description: 'Branch to build from',
          name: 'Branch'
        ]
      ]
    ]
  ]);
}

@NonCPS
def slackChangeLog() {
  def slackChangelog = ''
  def changeLogSets = currentBuild.rawBuild.changeSets
  for (int i = 0; i < changeLogSets.size(); i++) {
    def entries = changeLogSets[i].items
    for (int j = 0; j < entries.length; j++) {
      def entry = entries[j]
      echo "${entry.commitId} by ${entry.author} on ${new Date(entry.timestamp)}: ${entry.msg}"
      slackChangelog = "${slackChangelog}${entry.author}: ${entry.msg}\n"
      def files = new ArrayList(entry.affectedFiles)
      for (int k = 0; k < files.size(); k++) {
        def file = files[k]
        echo "  ${file.editType.name} ${file.path}"
        //slackChangelog = "${slackChangelog}  ${file.editType.name} ${file.path}\n"
      }
    }
  }
  slackSend channel: '#menupad-feed', color: 'warn', message: "*Changelog*\n```${slackChangelog}```", teamDomain: 'madmobile', token: 'NTI7azV0us2KQtf0TV1AKlFf'
}

node
{
  stage('Install')
  {
    deleteDir()
	
	if(env.BITBUCKET_SOURCE_BRANCH.toString() != 'null')
	{
	  //Branch = env.BITBUCKET_SOURCE_BRANCH.toString()
	  git url: 'git@bitbucket.org:madmobile/marvel-integration.git', branch: "${env.BITBUCKET_SOURCE_BRANCH}", changelog: true
	  
	  currentBuild.displayName = "#${currentBuild.number}  (Auto)"
	  
	  BranchToBuild = "${env.BITBUCKET_SOURCE_BRANCH}"
	}
	else
	{
	  git url: 'git@bitbucket.org:madmobile/marvel-integration.git', branch: Branch, changelog: true
	  
	  currentBuild.displayName = "#${currentBuild.number}  (Manual)"
	  
	  BranchToBuild = "${Branch}"
	}
  
    // Get the Version Number from pom.xml then append build number back to it
    def pom = readMavenPom()
    PackageVersion =  pom.version
	
	if(BranchToBuild.contains('demo/'))
	{
      def BranchArray = BranchToBuild.split('/')
	  PackageVersion = PackageVersion + '-' + BranchArray[1]
      pom.version = "${PackageVersion}"
	  echo pom.version
      writeMavenPom model: pom
	}
    
    currentBuild.description = "${BranchToBuild} ${PackageVersion}"
  
    slackSend channel: '#menupad-feed', color: 'warn', message: "Starting Marvel Integration build for ${BranchToBuild}: *${PackageVersion}* - (<${env.BUILD_URL}|View>)", teamDomain: 'madmobile', token: 'NTI7azV0us2KQtf0TV1AKlFf'
  
    // Use function above to generate a slack changelog
    slackChangeLog()
	
	stash name: 'MarvelArtifacts', includes: '*.*,*/**'
  }
  
  node('windowz')
  {
    stage('Build')
    {
	  deleteDir()
	  
	  unstash 'MarvelArtifacts'
	  
      // Maven Deploy
      sh 'mvn deploy'
    }
  }

  // Create a tag in the repo
  sh "git tag -a ${PackageVersion}-${env.BUILD_NUMBER} -m ${BranchToBuild}"
  sh "git push git@bitbucket.org:madmobile/marvel-integration.git ${PackageVersion}-${env.BUILD_NUMBER}"

  slackSend channel: '#menupad-feed', color: 'good', message: "Marvel Integration *${PackageVersion}*", teamDomain: 'madmobile', token: 'NTI7azV0us2KQtf0TV1AKlFf'
}
