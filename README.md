# 기술 스택
- 언어 : Java 17
- 프레임워크 : Spring Boot 3.4.2
- DB : H2 DataBase
- 문서화 : Swagger


# API 구현 설명
1. 헬스 체크 API
   ![스크린샷 2025-02-21 165357](https://github.com/user-attachments/assets/5c1a8494-c69e-4577-bb68-54d1736a60a4)
  - @RestController와 @GetMapping("/health")을 사용하여 간단한 상태 체크 엔드포인트를 제공합니다.
  - 응답은 ApiResponse 객체를 사용하여 200 OK 상태와 함께 메시지를 반환합니다.

2. 초기화 API
   ![스크린샷 2025-02-21 165611](https://github.com/user-attachments/assets/3fb73035-54cd-48e9-bf0a-b68e9f86e1eb)
   ![스크린샷 2025-02-21 165626](https://github.com/user-attachments/assets/708f0188-fa52-413c-a6b0-60ab52320c58)
  - H2 Database의 모든 데이터를 deleteAll()을 사용하여 초기화합니다.
  - WebClient를 이용해 Faker API에서 유저 데이터를 가져옵니다.
  - 받은 데이터를 User 엔티티로 변환 후 JPA를 통해 저장합니다.
  - fakerId 중복을 방지하기 위해 existsByFakerId()를 사용하여 체크 후 저장합니다.

3. 유저 전체 조회 API
   ![스크린샷 2025-02-21 165707](https://github.com/user-attachments/assets/919f7e9f-71bb-4c36-a0ad-38c8ee3f7d43)
  - Pageable을 활용하여 PageRequest.of(page, size, Sort.by("id").ascending())로 데이터를 가져옵니다.
  - UserResponse DTO를 사용하여 응답 형식을 맞췄습니다.

4. 방 생성 API
   ![스크린샷 2025-02-21 165754](https://github.com/user-attachments/assets/5a054386-2d6b-426c-980b-4179878887c8)
   ![스크린샷 2025-02-21 165804](https://github.com/user-attachments/assets/3b96653c-7289-4b1e-90e9-cb74f182b695)
  - Room.create() 팩토리 메서드를 활용하여 객체를 생성하고 저장합니다.
  - userRepository.findById()를 사용하여 유저 상태를 확인한 후 생성 여부를 결정합니다.

5. 방 전체 조회 API
   ![스크린샷 2025-02-21 165834](https://github.com/user-attachments/assets/11cb8eb6-f2d4-45e4-818c-df23a1c96eea)
   - findAll(Pageable)을 사용하여 정렬된 데이터를 반환합니다.
     
7. 방 상세 조회 API
   ![스크린샷 2025-02-21 165851](https://github.com/user-attachments/assets/9c711540-3ce0-4908-b75b-4969be99e0e9)
   - findById()로 방을 조회하고 DTO로 변환하여 응답합니다.
     
9. 방 참가 API
    ![스크린샷 2025-02-21 165916](https://github.com/user-attachments/assets/00d4754c-4a49-4415-8735-f16ed0147c38)
    ![스크린샷 2025-02-21 165926](https://github.com/user-attachments/assets/83bcd730-a28d-4a7a-9251-7c61671b7d52)
  - isFull(room), isJoinedRoom(user)을 체크 후 참가 가능 여부를 판단합니다.
  - 팀 배정은 RED → BLUE 순서로 이루어집니다.
    
8. 방 나가기 API
   ![스크린샷 2025-02-21 165946](https://github.com/user-attachments/assets/0947bd94-fb27-41c0-bcbd-ec9768fa9c01)
  ![스크린샷 2025-02-21 165955](https://github.com/user-attachments/assets/0f878c66-b809-45b5-bd66-542302ce6297)
  - userRoomRepository.deleteByUserIdAndRoomId()를 사용하여 참가 정보를 삭제합니다.
  - 호스트가 나갈 경우 방 상태를 FINISH로 변경합니다.
    
9. 게임 시작 API
    ![스크린샷 2025-02-21 170037](https://github.com/user-attachments/assets/885bc460-242b-48a2-8a1c-d79b94560a5d)
    ![스크린샷 2025-02-21 170046](https://github.com/user-attachments/assets/7885cdce-8fe8-4240-bd76-4c4a5750be65)
  - room.startGame()을 호출하여 PROGRESS 상태로 변경합니다.
  - @Scheduled을 사용하여 1분 후 자동으로 FINISH 상태로 변경됩니다.
    
10. 팀 변경 API
    ![스크린샷 2025-02-21 170213](https://github.com/user-attachments/assets/7f2971db-d84d-4db7-9f0c-a5b44b7d8e1f)
    ![스크린샷 2025-02-21 170221](https://github.com/user-attachments/assets/cc517d1b-27ce-4625-9b0f-6f6fb840c506)
    - userRoom.changeTeam()을 호출하여 변경된 값을 저장합니다.

