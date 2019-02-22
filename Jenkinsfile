pipeline {
   agent any

   stages {
      stage('clean check') {
         steps {
            sh """
               ./gradlew clean check
            """
         }
      }
   }
}
