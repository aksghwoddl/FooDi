# 푸디 (FOODI) 🍳
## <img width="5%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/207541502-c6208523-464c-4af4-8c37-c9bb2ee9c1b9.png">

<div align="center">
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/207541217-a42becc9-7624-45e2-a3d7-afe4896bec0d.png">
</div>

<br>

## 🤔 프로젝트 설명

> 식단관리를 간편하게!! 내손안에 식단 플래너 푸디(FOODIary)입니다. <br><br>
> 현재 server에 배포를 하지 않아 정상적인 기능 사용을 위해서는 dJango 설치 후 <br>
> local에서 back-end 어플리케이션을 실행해주어야 합니다. (/backend/FooDi_명세서.txt참고)

<br>

### 💻 기술스택 
#### ▪️ Client
<p>
<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=Android&logoColor=white">
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white">
</p>

##### - 사용 라이브러리
<p>
<img src="https://img.shields.io/badge/RoomDB-003B57?style=for-the-badge&logo=SQLite&logoColor=white">
<img src="https://img.shields.io/badge/RxBinding-B7178C?style=for-the-badge&logo=ReactiveX&logoColor=white">
<img src="https://img.shields.io/badge/Retrofit2-faff00?style=for-the-badge&logo=&logoColor=white">
<img src="https://img.shields.io/badge/MpAndriodChart-0F9D58?style=for-the-badge&logo=&logoColor=white">
<img src="https://img.shields.io/badge/DataBinding-0F9D58?style=for-the-badge&logo=&logoColor=white">
</p>

#### ▪️ Server
<p>
<img src="https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=Python&logoColor=white">
<img src="https://img.shields.io/badge/dJango-092E20?style=for-the-badge&logo=Django&logoColor=white">
<img src="https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=SQLite&logoColor=white">
</p>
<br>

### 🛠 구현 사항
##### 1️⃣ 음식 검색하기
###### Rest 통신을 통한 서버에 저장된 음식의 정보를 가져오는 기능
<div align="center">
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/207863055-f4ee8a91-57cf-475e-a3c2-0a1325b34d5a.gif">
</div>

##### 2️⃣ 다이어리
###### RoomDB를 통해 사용자가 섭취한 음식을 기록하는 기능
<p align="center">
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/207863284-a0ee937e-1514-4d3b-8ec3-149ae59e6b4d.gif">
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/207863737-b8744aee-e00d-43d9-9395-364a92433a71.gif">
</p>

##### 3️⃣ 음식 추가하기
###### Rest 통신을 통해 검색시에 존재하지 않는 음식을 사용자가 직접 추가하는 기능
<div align="center">
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/207865711-f172307a-a414-43be-9218-7b96d02dd02d.gif">
</div>


##### 4️⃣ 리포트
###### RoomDB에 저장된 데이터를 MPAndroidChart 라이브러리를 활용하여 그래프로 표시
<div align="center">
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/207864685-62c4098b-14ea-47f6-b28d-125041b6004a.gif">
</div>

##### 5️⃣ 설정 화면
###### 사용자가 설정한 옵션을 SharedPreference에 저장하여 사용하도록 구현
<div align="center">
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/207866066-0a459971-8da7-46ed-b65e-c5b4e2d1a085.gif">
</div>

##### 6️⃣ 식단 타이머
###### BroadcastReceiver , Alaram Manager , Pending Intent를 사용하여 사용자가 식단을 기록한 시간으로 부터 설정한 시간이 지나면 Notification을 보내는 기능
<div align="center">
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/207862665-867f14ca-7a75-44f3-9f8f-29c089b4117d.gif">
</div>

