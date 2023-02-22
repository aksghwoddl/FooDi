# 푸디 (FOODI) 🍳
## <img width="5%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/207541502-c6208523-464c-4af4-8c37-c9bb2ee9c1b9.png">

<div align="center">
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/208356950-ac21ba47-37cb-46b4-9e9d-c5044514c5db.png">
</div>

<br>

## 🤔 프로젝트 설명

> 식단관리를 간편하게!! 내손안에 식단 플래너 푸디(FOODIary)입니다. <br><br>
> 현재 server에 배포를 하지 않아 정상적인 기능 사용을 위해서는 dJango 설치 후 <br>
> local에서 back-end 어플리케이션을 실행해주어야 합니다. (/backend/FooDi_명세서.txt참고)<br><br>
> 아울러 푸디 관리자 앱을 통하여 저장된 음식 정보를 update , delete 할수 있습니다. (repository Foodi_admin 참고)

<br>

### 💻 기술스택 
#### ▪️ Client
<p>
<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=Android&logoColor=white">
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white">
<img src="https://img.shields.io/badge/Room-003B57?style=for-the-badge&logo=SQLite&logoColor=white">
<img src="https://img.shields.io/badge/RxBinding-B7178C?style=for-the-badge&logo=ReactiveX&logoColor=white">
<img src="https://img.shields.io/badge/Retrofit-3E4348?style=for-the-badge&logo=Square&logoColor=white">
<img src="https://img.shields.io/badge/OkHttp-3E4348?style=for-the-badge&logo=Square&logoColor=white">
<img src="https://img.shields.io/badge/DataBinding-0F9D58?style=for-the-badge&logo=&logoColor=white">
<img src="https://img.shields.io/badge/MVVM-0F9D58?style=for-the-badge&logo=&logoColor=white">
<img src="https://img.shields.io/badge/Coroutine-0F9D58?style=for-the-badge&logo=&logoColor=white">
<img src="https://img.shields.io/badge/MpAndriodChart-0F9D58?style=for-the-badge&logo=&logoColor=white">
<img src="https://img.shields.io/badge/Hilt-0F9D58?style=for-the-badge&logo=&logoColor=white">
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
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/209274826-55755f6e-fbc7-432b-ad65-64e5c147a50a.gif">
</div>

##### 2️⃣ 다이어리
###### RoomDB를 통해 사용자가 섭취한 음식을 기록하는 기능
<p align="center">
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/209274602-0c77f5c7-d212-441b-986a-d1321a89d999.gif">
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/207863737-b8744aee-e00d-43d9-9395-364a92433a71.gif">
</p>

##### 3️⃣ 음식 추가하기
###### Rest 통신을 통해 검색시에 존재하지 않는 음식을 사용자가 직접 추가하는 기능
<div align="center">
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/208650624-0b2091a8-fe09-4d59-a25e-4f9ff73fc8e3.gif">
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
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/208374244-5644b4df-93e2-4a65-826b-16809fa64666.gif">
</div>

### 😎 프로젝트 사용기술 설명
##### 1️⃣ Dagger Hilt를 활용하여 의존성을 주입 해주었습니다.
##### 2️⃣ MVVM 디자인 기반으로 프로젝트를 진행 하였습니다.
##### 3️⃣ Coroutine을 통한 비동기 처리를 , RxBinding을 통한 UI event 처리를 하였습니다. (throttleFirst()를 통한 이중 클릭 방지)
##### 4️⃣ Retrofit2를 통해 Rest통신을 하였습니다.
##### 5️⃣ Room을 활용하여 내부 저장소에 식단 정보를 저장하도록 구현 하였습니다.
##### 6️⃣ Repository를 사용하여 Data를 관리 하였습니다.
##### 7️⃣ MPAndroidChart 라이브러리를 통해 내부 저장소에 저장된 정보를 그래프로 시각화 하였습니다.
##### 8️⃣ SharedPreference를 활용한 설정 정보를 저장하여 앱 내부에서 모두 공유 할 수 있도록 하였습니다.
##### 9️⃣ Clean Architecture를 위해 Module화를 통해 각 Layer를 분리해주었습니다.
##### 🔟 ListAdapter , DiffUtil , AsyncListDiffer를 사용하여 RecyclerView의 Adapter를 구현 하였습니다.
