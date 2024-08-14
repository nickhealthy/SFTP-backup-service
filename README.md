## SFTP 백업: 자동 압축 및 파일 정리

---

SFTP (Jsch 라이브러리 사용)를 통해 원격지의 백업 대상 파일을 로컬로 백업하고, 백업한 파일을 압축한 후 원격지의 원본 파일을 삭제합니다. 또한, 로컬에서도 압축된 파일을 제외한 나머지 백업 원본 파일들을 삭제합니다. 모든 작업은 지정된 시간에 자동으로 스케줄링되어 실행됩니다.

### application.properties 설정
SFTP (Secure File Transfer Protocol)를 사용하여 파일을 관리합니다. SFTP 통합 및 백업 관리를 위해 application.properties 파일에 다음과 같은 설정 값을 지정해야 합니다.

```properties
## 복수개 백업 서버 설정

### [SERVER 1] ###
# SFTP 서버 호스트 이름
sftp.servers[0].host=localhost
# SFTP 서버 포트 번호 (기본값은 22)
sftp.servers[0].port=22
# SFTP 사용자 이름
sftp.servers[0].user=user
# SFTP 사용자 비밀번호
sftp.servers[0].password=1234
# 백업 파일 대상 원격 디렉토리 (SFTP 서버 내의 경로)
sftp.servers[0].remote-dir=/backup/logs
# 백업 파일을 저장할 로컬 디렉토리 (로컬 파일 시스템 내의 경로)
sftp.servers[0].local-dir=/backup/server1
# SFTP 서버의 인증 키 파일 경로 (비밀번호 대신 키 기반 인증을 사용할 때 개인 키 파일의 경로를 지정)
sftp.servers[0].prikey=C:\\Users\\nick\\.ssh\\id_rsa

### [SERVER 2] ###
# SFTP 서버 호스트 이름
sftp.servers[1].host=localhost
# SFTP 서버 포트 번호 (기본값은 22)
sftp.servers[1].port=21
# SFTP 사용자 이름
sftp.servers[1].user=user
# SFTP 사용자 비밀번호
sftp.servers[1].password=1234
# 백업 파일 대상 원격 디렉토리 (SFTP 서버 내의 경로)
sftp.servers[1].remote-dir=/backup/logs
# 백업 파일을 저장할 로컬 디렉토리 (로컬 파일 시스템 내의 경로)
sftp.servers[1].local-dir=/backup/server2
# SFTP 서버의 인증 키 파일 경로 (비밀번호 대신 키 기반 인증을 사용할 때 개인 키 파일의 경로를 지정)
sftp.servers[1].prikey=C:\\Users\\nick\\.ssh\\id_rsa
```