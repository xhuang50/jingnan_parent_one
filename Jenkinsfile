pipeline {
   agent  any

   options {
    	//超过5分钟没有构建完成会退出构建
        timeout(time: 30, unit: 'MINUTES')
        //保持构建的最大个数
        buildDiscarder(logRotator(numToKeepStr: '20'))
   }

   tools {
        //需要在jenkins配置maven环境，名称为maven3.6.1
        maven 'maven3.6.1'
   }

   // 常量参数
   environment{
		//工作空间绝对路径
		WORKSPACE_l='${WORKSPACE}'
		//手动拼接
		WORKSPACE_t='${JENKINS_HOME}/workspace/${JOB_NAME}'
		repo_code_dir='/usr/local/src/${artifactId}/${VERSION}/'

   }

   stages {
      stage('拉取代码') {
			when {
				expression {
					return  ("${DEPLOY}" == "upgrade")
				}
			}
            steps {
                git credentialsId: 'itxiongge', url: '${URL}', branch:"${BRANCH}"

            }
      }
      stage('测试代码') {
			when {
				expression {
					return  ("${DEPLOY}" == "upgrade")
				}
			}
            steps {
    	        //执行单元测试
                sh "mvn clean test -pl ${artifactId} -am"
    	        //单元测试报告位置
                junit '**/target/surefire-reports/*.xml'
            }
      }
      stage('打包服务') {
			when {
				expression {
					return  ("${DEPLOY}" == "upgrade")
				}
			}
            steps {
	        	//执行打包
            	sh "mvn clean package -Dmaven.test.skip=true  -pl ${artifactId} -am"
         	}
      }
      stage('移动至代码仓库') {
			when {
				expression {
					return  ("${DEPLOY}" == "upgrade")
				}
			}
            steps {
            	sh "mkdir -p ${repo_code_dir}"
	            //将打包后的项目启动
                sh "cp ${WORKSPACE_l}/${artifactId}/target/${artifactId}-${VERSION}.jar ${repo_code_dir}"
                sh "cp ${WORKSPACE_l}/${artifactId}/target/classes/startup.sh ${repo_code_dir}"
                sh "cp ${WORKSPACE_l}/${artifactId}/target/classes/stop.sh ${repo_code_dir}"
         }
      }
      stage('发布服务|回滚') {
        steps {
            script {
				//批量执行远程服务器发布命令：
				for(ip in ts_ips.tokenize(',')){
					//清空发布目标服务器文件夹
					sh "sshpass -p ${ts_pwd} ssh -p ${ts_port} ${ts_user}@${ip} 'rm -rf /usr/local/src/${artifactId}/latest/*'"
					sh "sshpass -p ${ts_pwd} scp -P ${ts_port} ${repo_code_dir}${artifactId}-${VERSION}.jar ${ts_user}@${ip}:/usr/local/src/${artifactId}/latest/"
					sh "sshpass -p ${ts_pwd} scp -P ${ts_port} ${repo_code_dir}startup.sh ${ts_user}@${ip}:/usr/local/src/${artifactId}/latest/"
					sh "sshpass -p ${ts_pwd} scp -P ${ts_port} ${repo_code_dir}stop.sh ${ts_user}@${ip}:/usr/local/src/${artifactId}/latest/"
				}
            }
        }
      }
      stage('启动服务') {
        steps {
            script {
				//批量执行远程服务器发布命令：
				for(ip in ts_ips.tokenize(',')){
					sh "sshpass -p ${ts_pwd} ssh -p ${ts_port} ${ts_user}@${ip} 'chmod 755 /usr/local/src/${artifactId}/latest//stop.sh'"
					sh "sshpass -p ${ts_pwd} ssh -p ${ts_port} ${ts_user}@${ip} '/usr/local/src/${artifactId}/latest//stop.sh'"
					sh "sshpass -p ${ts_pwd} ssh -p ${ts_port} ${ts_user}@${ip} 'chmod 755 /usr/local/src/${artifactId}/latest//startup.sh'"
					sh "sshpass -p ${ts_pwd} ssh -p ${ts_port} ${ts_user}@${ip} '/usr/local/src/${artifactId}/latest//startup.sh'"
				}
            }
        }
      }
   }
}
